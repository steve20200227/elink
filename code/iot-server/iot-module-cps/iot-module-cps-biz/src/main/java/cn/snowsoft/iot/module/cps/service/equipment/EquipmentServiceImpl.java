package cn.snowsoft.iot.module.cps.service.equipment;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.snowsoft.iot.framework.common.exception.ServiceException;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentRelateWarningDTO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPage;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.EquipmentAgreementTypeVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.EquipmentStatusVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.cpsRule.CpsRule;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.dto.EquipmentBatchAddDTO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import cn.snowsoft.iot.module.cps.dal.mysql.equipment.EquipmentMapper;
import cn.snowsoft.iot.module.cps.ekuiperClient.MonitorClient;
import cn.snowsoft.iot.module.cps.emqx.api.AuthenticationApi;
import cn.snowsoft.iot.module.cps.initServer.cache.ServerCache;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.SeriesDatasource;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseColumn;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseSeriesTable;
import cn.snowsoft.iot.module.cps.service.cpsRule.CpsRuleService;
import cn.snowsoft.iot.module.cps.service.product.ProductService;
import cn.snowsoft.iot.module.warning.enums.api.config.AdminConfigApi;
import cn.snowsoft.iot.module.warning.enums.api.config.vo.ConfigCountVO;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.xml.stream.events.Comment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, EquipmentDO> implements EquipmentService {

    @Resource
    private EquipmentPointService pointService;

    @Autowired
    private MonitorClient monitorClient;

    @Autowired
    private ProductService productService;

    @Resource
    private AdminConfigApi configApi;

    @Resource
    private CpsRuleService cpsRuleService;

    @Value("${ekuiper.url}")
    private String ekuiperUrl;

    @Value("${ekuiper.ws}")
    private String ws;

    @Value("${ekuiper.sql}")
    private String ekuiperSql;

    @Value("${ekuiper.attributeSourceConfkey:attributeSource}")
    private String attributeSourceConfkey;
    @Value("${ekuiper.eventSourceConfkey:eventSource}")
    private String eventSourceConfkey;
    @Value("${ekuiper.actions.path}")
    private String ekuiperWebSocketPath;

    @Autowired
    private SeriesDatasource seriesDatasource;
    @Autowired
    private RedisCacheManager redisCacheManager;
    private String deviceQRCode;
    @Override
    public IPage<EquipmentPage> selectEquipmentPage(EquipmentPageVO equipment) {
        IPage<EquipmentPage> page = new Page<>(equipment.getPageNo(), equipment.getPageSize());
        IPage<EquipmentPage> equipmentDOIPage = baseMapper.selectEquipmentPage(page, equipment);
        List<EquipmentPage> records = equipmentDOIPage.getRecords();
        List<String> equipmentCodeList = new ArrayList<>();

        //从redis中获取当前设备的状态、网络位置、最后活跃时间
        Cache deviceStatusCache = redisCacheManager.getCache("deviceStatus");
        records.forEach(item -> {
            String info = deviceStatusCache.get(item.getEquipmentCode(), String.class);
            if (StringUtil.isNotBlank(info)) {
                String[] split = info.split("-");
                //设备状态
                item.setStatus(Integer.parseInt(split[0]));
                //网络位置
                item.setNetLocation(split[1]);
                //最后活跃时间
                item.setLastActivated(new Date(Long.parseLong(split[2])));
            } else {
                item.setStatus(0);
                item.setLastActivated(null);
                item.setNetLocation("");
            }
            equipmentCodeList.add(item.getEquipmentCode());
        });
        if (equipmentCodeList.size() > 0) {
            List<ConfigCountVO> warningCount = configApi.getWarningCount(equipmentCodeList);
            // 创建一个Map来快速查找ConfigCountVO对象
            Map<String, Integer> configCountMap = new HashMap<>();
            for (ConfigCountVO configCount : warningCount) {
                configCountMap.put(configCount.getEquipmentCode(), configCount.getWarningCount());
            }
            // 遍历EquipmentDO列表，并更新warningCount
            for (EquipmentPage equipmentPage : records) {
                Integer count = configCountMap.get(equipmentPage.getEquipmentCode());
                if (count != null) {
                    equipmentPage.setWarningCount(count);
                } else {
                    equipmentPage.setWarningCount(0);
                }
            }
        }
        page.setRecords(records);
        return equipmentDOIPage;
    }

    @Override
    public List<EquipmentDO> selectEquipmentList(EquipmentDO equipment) {
        List<EquipmentDO> equipmentDOList = ObjectUtil.defaultIfNull(lambdaQuery().eq(EquipmentDO::getIsEnable, equipment.getIsEnable()).list(), new ArrayList<>());
        return equipmentDOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean enableEquipment(List<Long> ids) {
        List<EquipmentDO> equipmentDOS = this.listByIds(ids);
        if (ObjectUtils.isNotEmpty(equipmentDOS)) {
            List<String> productCodeList = new ArrayList<>();
            for (EquipmentDO equipment : equipmentDOS) {
                //如果未开启历史数据缓存  则不创建dp表
                if("1".equals(equipment.getIsHistory())){
                    Boolean table = createSeriesTable(equipment);
                    log.info("创建时序数据库表结果：{}",table);
                }
                //在流计算引擎中增加该设备属性上报的mqtt源，供后续设备监控和场景使用
                // 首先确定计算源的构建sql片段
                String calcStreamSql = "CREATE STREAM streamParam () WITH (DATASOURCE=\"topicParam\", CONF_KEY=\"" + attributeSourceConfkey + "\", TYPE=\"mqtt\", SHARED=\"true\", FORMAT=\"json\");";
                // 创建计算源    命名规则 AttributePush_设备编码
                String streamParam = "AttributePush_" + equipment.getEquipmentCode();
                JSONObject calcStream = new JSONObject();
                calcStream.put("sql", calcStreamSql.replace("streamParam", streamParam).replace("topicParam", equipment.getAttributePush()));
                MonitorClient.streamCreation(calcStream, ekuiperUrl);

                //在流计算引擎中增加该设备事件上报的mqtt源
                // 首先确定计算源的构建sql片段
                String eventStreamSql = "CREATE STREAM streamParam () WITH (DATASOURCE=\"topicParam\", CONF_KEY=\"" + eventSourceConfkey + "\", TYPE=\"mqtt\", SHARED=\"true\", FORMAT=\"json\");";
                // 创建计算源    命名规则 EventPush_设备编码
                String eventParam = "EventPush_" + equipment.getEquipmentCode();
                JSONObject eventStream = new JSONObject();
                eventStream.put("sql", eventStreamSql.replace("streamParam", eventParam).replace("topicParam", equipment.getEventPush()));
                MonitorClient.streamCreation(eventStream, ekuiperUrl);
                // 启用,前端系统字典的is_enable的1代表启用
                equipment.setIsEnable(1);
                if (updateById(equipment)) {
                    productCodeList.add(equipment.getProductCode());
                    ServerCache.deviceCodeAttributeTopic.put(equipment.getEquipmentCode(), equipment.getAttributePush());
                    ServerCache.deviceCodeFeatureTopic.put(equipment.getEquipmentCode(), equipment.getFeatureIssued());
                    //对内存模式的设备属性上报topic集合进行更新操作，主要用于设备持久化规则更新时使用
                    updateInitHistorySet(equipment);
                }
            }
            try {
                ServerCache.updateAttributeRabbitBridgeHistory();
                //针对mqtt协议的设备，启用协议转换规则
                cpsRuleService.enabledOrDisabledRules(ids, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    //构造创建时序数据库表所需数据信息
    private Boolean createSeriesTable(EquipmentDO equipment) {
        if (seriesDatasource == null){
            log.info("未配置数据源");
            return true;
        }
        BaseSeriesTable baseSeriesTable = new BaseSeriesTable();
        //查询当前设备的所有属性点位，用作表的列
        List<EquipmentPointDO> equipmentPoints = pointService.lambdaQuery().eq(EquipmentPointDO::getPointType, "pointStats").eq(EquipmentPointDO::getEquipmentId, equipment.getId()).list();
        //将设备编码作为表名称
        baseSeriesTable.setTableName(equipment.getEquipmentCode());
        List<BaseColumn> list = new ArrayList<>();
        for (EquipmentPointDO equipmentPoint : equipmentPoints) {
            BaseColumn column = new BaseColumn();
            column.setColumnName(equipmentPoint.getPointCode());
            column.setColumnType(equipmentPoint.getDataType());
            list.add(column);
        }
        baseSeriesTable.setTableColumns(list);
        Boolean table = seriesDatasource.createTable(baseSeriesTable);
        return table;
    }

    @Override
    public Boolean disenableEquipment(List<Long> ids) {
        List<EquipmentDO> equipmentDOS = this.listByIds(ids);
        if (ObjectUtils.isNotEmpty(equipmentDOS)) {
            for (EquipmentDO equipment : equipmentDOS) {
                //尝试删除kuiper监控规则
                MonitorClient.ruleDelete("monitor-" + equipment.getEquipmentCode(), ekuiperUrl);
                // 在流计算引擎中删除该设备的属性上报源
                MonitorClient.streamDelete("AttributePush_" + equipment.getEquipmentCode(), ekuiperUrl);
                // 在流计算引擎中删除该设备的事件上报源
                MonitorClient.streamDelete("EventPush_" + equipment.getEquipmentCode(), ekuiperUrl);
                // 从全局已启用设备缓存中移除
                ServerCache.deviceCodeAttributeTopic.remove(equipment.getEquipmentCode());
                ServerCache.deviceCodeFeatureTopic.remove(equipment.getEquipmentCode());
            }

            //针对mqtt协议的设备，禁用协议转换规则
            try {
                cpsRuleService.enabledOrDisabledRules(ids, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return lambdaUpdate().in(EquipmentDO::getId, ids).set(EquipmentDO::getIsEnable, 0).update();
        }
        return true;
    }

    @Override
    public Boolean removeEquipment(EquipmentPage equipment) {
        // 如果该设备具有emqx的认证账号，则先移除这个账号
        //设备生成时 判断产品是否有该账号 有则不删除
        //如果账号密码不为空  那就走判断
        if (ObjectUtils.isNotEmpty(equipment.getUserAccount()) && ObjectUtils.isNotEmpty(equipment.getUserPassword())){
            LambdaQueryWrapper<ProductDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ProductDO::getUserAccount, equipment.getUserAccount());
            List<ProductDO> list = productService.list(lambdaQueryWrapper);
            //如果产品没有该账号 则删除
            if (CollectionUtils.isEmpty(list)){
                if (StringUtils.isNotBlank(equipment.getUserAccount())) {
                    try {
                        AuthenticationApi.removeAuthentication(equipment.getUserAccount());
                    } catch (Exception e) {
                        throw new ServiceException(500,"删除账号失败");
                    }
                }
            }
        }

        //删除设备的转换规则，此处只需要删除数据库记录即可，EMQX中的对应规则已经在设备失效时删除
        LambdaQueryWrapper<CpsRule> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(CpsRule::getRelevanceId, equipment.getId());
        cpsRuleService.remove(lambdaQuery);

        Long id = equipment.getId();
        pointService.removePoint(id);
        baseMapper.deleteById(id);

        //从内存中移除该设备的编码ID映射关系
        ServerCache.deviceCodeID.remove(equipment.getEquipmentCode());
        return true;
    }

    @Override
    public Object restart() {
        // todo 重启功能需要重写
        // 查询所有已启用的设备信息
        List<EquipmentDO> lists = lambdaQuery().eq(EquipmentDO::getIsEnable, 1).list();
        if (lists.isEmpty()) {
            return false; // 如果没有已启用的设备，直接返回
        }
        List<Long> ids = lists.stream().map(EquipmentDO::getId).collect(Collectors.toList());

        // 将查询的设备全部失效
        LambdaUpdateWrapper<EquipmentDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(EquipmentDO::getIsEnable, 0).in(EquipmentDO::getId, ids);
        boolean updateSuccess = this.update(lambdaUpdateWrapper);

        if (updateSuccess) {
            // 将查询的设备全部启用
            enableEquipment(ids);
        } else {
            // 处理更新失败的情况，例如记录日志或者抛出异常
            log.error("Failed to disable equipment");
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult submit(EquipmentDO equipment) {
        List<EquipmentDO> equipmentList = checkCode(equipment);
        if (equipmentList.size() > 0) {
//            throw new RuntimeException("设备编码已存在: " + equipment.getEquipmentCode());
            return CommonResult.error(207, "设备编码已存在");
        }
        if (equipment.getId() == null) {
            equipment.setEquipmentAscription("none");
            equipment.setQrCode(twoDimensionalCode("{equipmentCode:"+equipment.getEquipmentCode()+"}"));
        }
        if (equipment.getAgreementType() == null) {
            equipment.setAgreementType("");
        }
        saveOrUpdate(equipment);
        ProductDO productDO = productService.getByCode(equipment.getProductCode());
        pointService.coveragePoint(equipment.getId(), productDO.getId());
        //从内存中增加该设备的编码ID映射关系
        ServerCache.deviceCodeID.put(equipment.getEquipmentCode(), equipment.getId());
        return CommonResult.success(equipment);
    }
    /**
     * 将JSONObject转换为请求路径的方法
     *
     * @param jsonObject
     * @return
     */
    private static String convertToRequestPath(cn.hutool.json.JSONObject jsonObject) {
        StringBuilder pathBuilder = new StringBuilder();

        // 遍历JSONObject中的键值对
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            // 对值进行URL编码
            try {
                value = URLEncoder.encode(value.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 将键值对添加到请求路径中
            pathBuilder.append(key).append("=").append(value).append("&");
        }

        // 删除最后一个"&"
        pathBuilder.deleteCharAt(pathBuilder.length() - 1);

        return pathBuilder.toString();
    }
    public String twoDimensionalCode(String code) {
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject(code);
        //构造请求方法体
        String convertToRequestPath = convertToRequestPath(jsonObject);
        String data = deviceQRCode + convertToRequestPath; // 要编码的数据

        int width = 300;
        int height = 300;

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);

            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix); // 将 BitMatrix 转换为 BufferedImage

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos); // 将 BufferedImage 写入 ByteArrayOutputStream

            byte[] imageBytes = baos.toByteArray(); // 获取二维码图片的字节数组

            String base64Image = Base64.getEncoder().encodeToString(imageBytes); // 将字节数组转换为 Base64 编码的字符串

            return base64Image;
        } catch (Exception e) {
            System.err.println("生成二维码时出现异常：" + e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean relevancyEquipment(List<Long> ids, Long passageId) {
        return baseMapper.relevancyEquipment(ids, passageId);
    }

    @Override
    public Boolean deleteEquipmentByPassageId(Long id) {
        baseMapper.deleteEquipmentByPassageId(String.valueOf(id));
        return true;
    }

    /**
     * 当数据监控界面中选择某个设备后，调用此逻辑实现单个设备的点位数据监控
     * 此业务包含两部分 1.查询设备信息 2.根据设备创建ekuiper规则，借助ekuiper的websocket动作进行实时数据推送前端
     * @param id
     * @return
     */
    @Override
    public EquipmentDO detailMonitor(Long id) {
        //对于ekuiper的websocket的服务端而言，所有设备的数据都会从一个路径发送，所以单个设备的客户端其实会接收到所有数据，
        //所以此处针对每个设备进行独立端点的建立，使用设备编码作为websocket服务端的路径：/monitor/data/设备编码
        //这样所有设备监控共用一个ws服务端，但每个设备使用独立端点
        //根据设备id获取设备基础信息
        EquipmentDO equipmentDO = getById(id);
        //根据设备编码调用ekuiper接口，生成一个输出动作为websocket的规则，规则名称取 设备编码+_Monitor  动作作为websocket服务端，供前端数采监控连接
        JSONObject actionParam = new JSONObject();
        actionParam.put("path", ekuiperWebSocketPath + equipmentDO.getEquipmentCode());
        actionParam.put("insecureSkipVerify", true);
        actionParam.put("sendSingle", true);

        JSONObject action = new JSONObject();
        action.put("websocket", actionParam);

        JSONArray actions = new JSONArray();
        actions.add(action);

        JSONObject data = new JSONObject();
        //规则id，唯一   monitor-设备编码
        data.put("id", "monitor-" + equipmentDO.getEquipmentCode());
        //规则名称，数据监控-设备名称
        data.put("name", "数据监控-" + equipmentDO.getEquipmentName());
        data.put("sql", ekuiperSql.replace("param", "AttributePush_" + equipmentDO.getEquipmentCode()));
        data.put("actions", actions);
        monitorClient.ruleCreation(data, ekuiperUrl);
        equipmentDO.setWebSocket(ws + ekuiperWebSocketPath + equipmentDO.getEquipmentCode());
        return equipmentDO;
    }

    @Override
    public List<EquipmentPage> selectEquipmentPageByProductCode(EquipmentPageVO equipment) {
//        IPage<EquipmentPage> page = new Page<>(equipment.getPageNo(), equipment.getPageSize());
        List<EquipmentPage> records = baseMapper.selectEquipmentPageByProductCode(equipment);
//        List<EquipmentPage> records = equipmentDOIPage.getRecords();
        List<Long> ids = new ArrayList<>();
        records.forEach(item -> {
            ids.add(item.getId());
        });
        if (ids.size() > 0) {
//            List<ConfigCountVO> warningCount = configApi.getWarningCount(ids);
            // 创建一个Map来快速查找ConfigCountVO对象
            Map<Long, Integer> configCountMap = new HashMap<>();
//            for (ConfigCountVO configCount : warningCount) {
//                configCountMap.put(configCount.getEquipmentId(), configCount.getWarningCount());
//            }
            // 遍历EquipmentDO列表，并更新warningCount
            for (EquipmentPage equipmentPage : records) {
                Integer count = configCountMap.get(equipmentPage.getId());
                if (count != null) {
                    equipmentPage.setWarningCount(count);
                } else {
                    equipmentPage.setWarningCount(0);
                }
            }
        }
//        page.setRecords(records);
        return records;
    }

    private List<EquipmentDO> checkCode(EquipmentDO equipment) {
        LambdaQueryWrapper<EquipmentDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (equipment.getEquipmentCode().isEmpty()) {
            throw new RuntimeException("设备编码不能为空");
        }
        lambdaQueryWrapper.eq(EquipmentDO::getEquipmentCode, equipment.getEquipmentCode());
        //id不为空 是修改 排除当前数据
        if (equipment.getId() != null) {
            lambdaQueryWrapper.ne(EquipmentDO::getId, equipment.getId());
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<EquipmentAgreementTypeVO> getAgreementTypeCount() {
        return baseMapper.getAgreementTypeCount();
    }

    @Override
    public List<EquipmentRelateWarningDTO> getEquipmentName(List<Long> equipmentIds) {
        return baseMapper.getEquipmentName(equipmentIds);
    }

    @Override
    public List<EquipmentStatusVO> getIsEnableCount() {
        return baseMapper.getIsEnableCount();
    }

    @Override
    public CommonResult cancelPassage(String id) {
        boolean status = baseMapper.cancelPassage(id);
        return CommonResult.success(status);
    }

    @Override
    public List<EquipmentDO> getEnableList() {
        return baseMapper.selectList(new LambdaQueryWrapper<EquipmentDO>().eq(EquipmentDO::getIsEnable, 1));
    }

    @Override
    public List<EquipmentDO> getByIds(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return baseMapper.selectList(new LambdaQueryWrapper<EquipmentDO>().in(EquipmentDO::getId, list));
    }

    @Override
    public CommonResult getChildEquipment(String equipmentCode) {
        List<EquipmentDO> equipmentDOS = baseMapper.getChildEquipment(equipmentCode);
        if (ObjectUtils.isNotEmpty(equipmentDOS)) {
            return CommonResult.success(true);
        } else {
            return CommonResult.success(false);
        }
    }

    @Override
    public IPage<EquipmentDO> selectRuleList(EquipmentPageVO equipment) {
        IPage<EquipmentPage> page = new Page<>(equipment.getPageNo(), equipment.getPageSize());
        return baseMapper.selectRuleList(page,equipment);
    }

    @Override
    public CommonResult addAuthentication(EquipmentPage equipment) {
        //如果为空  说明第一次创建 走新增 不为空则先判断产品是否有关联  没有就删除
        if (ObjectUtils.isNotEmpty(equipment.getUserAccount()) && ObjectUtils.isNotEmpty(equipment.getUserPassword())){
            //设备生成时 判断产品是否有该账号 有则不删除
            LambdaQueryWrapper<ProductDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ProductDO::getUserAccount, equipment.getUserAccount());
            List<ProductDO> list = productService.list(lambdaQueryWrapper);
            //如果没数据则删除
            if (CollectionUtils.isEmpty(list)){
                // 先删除原来的认证器
                try {
                    EquipmentPage equipmentDO = new EquipmentPage();
                    equipmentDO.setUserAccount(equipment.getUserAccount());
                    equipmentDO.setUserPassword(equipment.getUserPassword());
                    AuthenticationApi.removeAuthentication(equipmentDO.getUserAccount());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ServiceException(500,"删除原账号失败");
                }

                try {
                    //生成新的账号密码
                    equipment.setUserAccount("device_" + generateCode());
                    equipment.setUserPassword("device_" + generateCode());
                    AuthenticationApi.insertAuthentication(equipment.getUserAccount(), equipment.getUserPassword());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ServiceException(500,"生成账号密码失败");
                }
            }
        }else {
            try {
                //生成新的账号密码
                equipment.setUserAccount("device_" + generateCode());
                equipment.setUserPassword("device_" + generateCode());
                AuthenticationApi.insertAuthentication(equipment.getUserAccount(), equipment.getUserPassword());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(500,"生成账号密码失败");
            }
        }
        return this.submit(equipment);
    }

    public String generateCode(){
        String result = "";
        String code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(code.length());
            result += code.charAt(index);
        }
        return result;
    }

    /**
     * 根据设备当前是否持久化数据动态更新内存historySet集合
     * @param equipment
     */
    public void updateInitHistorySet(EquipmentDO equipment){
        //如果开启历史数据缓存 则新增到内存historySet集合
        if("1".equals(equipment.getIsHistory())){
            ServerCache.historySet.add(equipment.getAttributePush());
        } else {
            //未启用则从内存historySet集合中删除
            ServerCache.historySet.remove(equipment.getAttributePush());
        }
    }

    @Override
    public CommonResult batchAdd(EquipmentBatchAddDTO equipment) {
        // 校验设备编码是否存在
        List<String> equipmentCodes = new ArrayList<>();
        for (int i = 1; i <= equipment.getEquipmentNum(); i++) {
            equipmentCodes.add((ObjectUtils.isNotEmpty(equipment.getEquipmentCodePrefixes()) ?
                    (equipment.getEquipmentCodePrefixes() + "_") : "") + i +
                    (ObjectUtils.isNotEmpty(equipment.getEquipmentCodeSuffix()) ?
                    ("_" + equipment.getEquipmentCodeSuffix()) : ""));
        }
        List<EquipmentDO> list = lambdaQuery().in(EquipmentDO::getEquipmentCode, equipmentCodes).list();
        if (list != null && list.size() > 0) {
            String res = list.stream().map(EquipmentDO::getEquipmentCode).collect(Collectors.joining(", "));
            return CommonResult.error(207, "新建的设备编码 [" + res + "]已存在，请合理配置编码前后缀");
        }
        // 构建批量新增数据
        List<EquipmentDO> equipmentDOS = new ArrayList<>();
        for (int i = 1; i <= equipment.getEquipmentNum(); i++) {
            EquipmentDO equipmentDO = new EquipmentDO();
            BeanUtil.copyProperties(equipment, equipmentDO);
            equipmentDO.setEquipmentCode((ObjectUtils.isNotEmpty(equipment.getEquipmentCodePrefixes()) ?
                    (equipment.getEquipmentCodePrefixes() + "_") : "") + i +
                    (ObjectUtils.isNotEmpty(equipment.getEquipmentCodeSuffix()) ?
                            ("_" + equipment.getEquipmentCodeSuffix()) : ""));
            equipmentDO.setEquipmentName(equipment.getEquipmentNamePrefixes() + i + equipment.getEquipmentNameSuffix());
            equipmentDO.setEquipmentAscription("none");
            equipmentDO.setQrCode(twoDimensionalCode("{equipmentCode:"+equipmentDO.getEquipmentCode()+"}"));
            equipmentDO.setAttributePush(equipment.getAttributePush().replace("equipmentCode", equipmentDO.getEquipmentCode()));
            equipmentDO.setEventPush(equipment.getEventPush().replace("equipmentCode", equipmentDO.getEquipmentCode()));
            equipmentDO.setFeatureIssued(equipment.getFeatureIssued().replace("equipmentCode", equipmentDO.getEquipmentCode()));
            equipmentDOS.add(equipmentDO);
        }
        saveOrUpdateBatch(equipmentDOS);
        // 批量添加设备点位
        for (EquipmentDO equipmentDO : equipmentDOS) {
            ProductDO productDO = productService.getByCode(equipmentDO.getProductCode());
            pointService.coveragePoint(equipment.getId(), productDO.getId());
            // 从内存中增加该设备的编码ID映射关系
            ServerCache.deviceCodeID.put(equipmentDO.getEquipmentCode(), equipmentDO.getId());
        }
        return CommonResult.success("操作成功");
    }
}
