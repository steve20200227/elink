package cn.snowsoft.iot.module.warning.service.warning.config;

import cn.hutool.json.JSONUtil;
import cn.snowsoft.iot.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.api.equipment.AdminEquipmentApi;
import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentDTO;
import cn.snowsoft.iot.module.job.api.XxljobAdminApi;
import cn.snowsoft.iot.module.job.dto.XxlJobEntity;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.*;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.EquipmentRelateWarningVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.WarningConfigStatusVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigRule;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.WarningDO;
import cn.snowsoft.iot.module.warning.dal.mysql.warning.config.WarningMapper;
import cn.snowsoft.iot.module.warning.ekuiperClient.WarningClient;
import cn.snowsoft.iot.module.warning.enums.api.config.vo.ConfigCountVO;
import cn.snowsoft.iot.module.warning.event.WarningEventHandler;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class WarningServiceImpl extends ServiceImpl<WarningMapper, WarningDO> implements IWarningService {

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Value("${ekuiper.url:}")
    private String url;

    @Value("${ekuiper.startSql:}")
    private String startSql;

    @Value("${ekuiper.ruleSql:}")
    private String ruleSql;

    @Value("${ekuiper.endSql:}")
    private String endSql;

    @Value("${ekuiper.actions.server:}")
    private String actionsServer;

    @Value("${ekuiper.actions.connectionSelector:}")
    private String connectionSelector;

    @Value("${ekuiper.actions.attributeTopic:}")
    private String attributeTopic;

    @Value("${ekuiper.actions.eventTopic:}")
    private String eventTopic;

    @Value("${ekuiper.actions.username:}")
    private String actionsUsername;

    @Value("${ekuiper.actions.password:}")
    private String actionsPassword;

    @Value("${ekuiper.actions.sendSingle:}")
    private Boolean sendSingle;

    @Value("${job.appName}")
    private String appName;

    @Value("${job.executorHandler}")
    private String executorHandler;

    @Value("${job.jobType}")
    private String jobType;

    @Resource
    private XxljobAdminApi xxljobAdminApi;

    @Resource
    private IConfigActionService configActionService;

    @Resource
    private AdminEquipmentApi equipmentApi;

    @Resource
    private WarningEventHandler warningHandler;

    @Override
    public IPage<WarningDO> selectWarningPage(WarningPageVO warningPageVO) {
        IPage<WarningDO> page = new Page<>(warningPageVO.getPageNo(), warningPageVO.getPageSize());
        return baseMapper.selectProductPage(page, warningPageVO);
    }

    /**
     * 场景配置保存，1：条件场景 2：定时场景 3：手动场景
     * @param warningSaveVO
     * @return
     */
    @Override
    public CommonResult<WarningDO> saveWarningConfig(WarningSaveVO warningSaveVO) {
        switch (warningSaveVO.getWarningType()) {
            //条件场景
            case "1":
                //1.处理规则引擎规则生成
                if (processWarningRule(warningSaveVO)) {
                    //2.处理业务数据持久化
                    warningSaveVO.setIsEnable(0);
                    if (saveOrUpdate(warningSaveVO)) {
                        warningSaveVO.getActionDOList().forEach(item -> {
                            item.setId(null);
                            item.setConfigId(warningSaveVO.getId());
                        });
                        configActionService.updateAction(warningSaveVO);
                        return CommonResult.success(warningSaveVO);
                    }
                    return CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), "保存失败");
                }
                return CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), "条件规则生成错误");
            //定时场景
            case "2":
                //2.处理业务数据持久化
                warningSaveVO.setIsEnable(0);
                if (saveOrUpdate(warningSaveVO)) {
                    //刷新动作列表
                    warningSaveVO.getActionDOList().forEach(item -> {
                        item.setId(null);
                        item.setConfigId(warningSaveVO.getId());
                    });
                    configActionService.updateAction(warningSaveVO);
                    return CommonResult.success(warningSaveVO);
                }
                return CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), "保存失败");
            //手动场景
            case "3":
                //2.处理业务数据持久化
                warningSaveVO.setIsEnable(0);
                if (saveOrUpdate(warningSaveVO)) {
                    warningSaveVO.getActionDOList().forEach(item -> {
                        item.setId(null);
                        item.setConfigId(warningSaveVO.getId());
                    });
                    configActionService.updateAction(warningSaveVO);
                    return CommonResult.success(warningSaveVO);
                }
                return CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), "保存失败");
            default:
                break;
        }

      return CommonResult.success(warningSaveVO);
    }


    /**
     * 用于特殊处理设备事件的条件场景，此类型的ekuiper源会使用独有的rabbit连接器 连接器名称是 eventIn
     * @param warningSaveVO
     * @param equipmentCodeNameMap
     * @return
     */
    private boolean processWarningRuleEvent(WarningSaveVO warningSaveVO, Map<String, String> equipmentTopic, Map<String, String> equipmentCodeNameMap) {
        List<ConfigRule> configRule = warningSaveVO.getWhereList();
        //如果条件未配置或者配置的条件数量不是1条，则直接返回
        if (ObjectUtils.isEmpty(configRule) || configRule.size() != 1) {
            return Boolean.FALSE;
        }
        // 处理流计算规则sql,此处判断上报的事件消息体中event字段的值必须是配置的事件code
        String whereSql = " WHERE (msg.eventCode = '" + configRule.get(0).getPointCode() + "') AND (msg.deviceCode = 'deviceParam')"
                    + " and substring(msg.thetime, 11, 19) between " + "'" + warningSaveVO.getBeginTime() + "'" + " and " + "'" + warningSaveVO.getEndTime() + "'";

        List<String> selectList = new ArrayList<>();
        //查询字段列表增加时间字段
        selectList.add("msg.thetime AS thetime");
        // 增加设备信息字段
        selectList.add("equipmentParam AS deviceCode");
        selectList.add("msg.eventCode AS eventCode");
        selectList.add("msg AS payload");
        selectList.add("nameParam AS deviceName");
        //生成一个唯一id，用于标识这个规则sql属于哪条场景配置，在后续动作执行时使用
        String ruleId;
        if (StringUtils.isNotBlank(warningSaveVO.getRuleId())) {
            ruleId = warningSaveVO.getRuleId();
        } else {
            ruleId = generateUniqueId();
        }
        selectList.add(ruleId + " ");
        //凭借固定sql片段，如select、from、以及select字段列表
        String sql = startSql + " " + String.join(", ", selectList) + ruleSql + whereSql;

        //声明计算规则列表，用于存储需要调用计算引擎生成的规则或数据源
        JSONArray rules = new JSONArray();
        // 处理消息收敛配置,主要是将计算引擎中原本一条规则记录，拆分成两个规则，两个规则之间使用引擎的内存主题进行连接
        if ("0".equals(warningSaveVO.getActionProcessType())) {
            //未配置消息收敛则，正常生成规则即可
            // 遍历适用设备列表，为每个设备进行流计算引擎接口调用，生成该场景的流计算规则。计算源使用设备的事件上报源
            warningSaveVO.getEquipmentList().forEach(equipment -> {
                //创建计算规则
                JSONObject jsonObject = new JSONObject();
                //todo 设置输出动作，后续考虑用户自主可配
                settingsMQTTAction(jsonObject, eventTopic);
                //规则id格式规定为   scene-场景记录ruleId(前面已通过代码生成)-设备编码
                jsonObject.put("id", "scene-" + ruleId + "-" + equipment);
                //规则名称格式规定为  场景-场景名称-设备编码
                jsonObject.put("name", "场景-" + warningSaveVO.getWarningName() + "-" + equipment);
                String equipmentCode = "'" + equipment + "'";
                String equipmentName = "'" + equipmentCodeNameMap.get(equipment) + "'";
                jsonObject.put("sql", sql.replace("rabbit_origin", "EventPush_" + equipment).replace("deviceParam", equipment).replace("equipmentParam", equipmentCode).replace("nameParam", equipmentName));
                JSONObject temp1 = new JSONObject();
                temp1.put("type", "ruleCreation");
                temp1.put("data", jsonObject);
                temp1.put("id", jsonObject.getString("id"));
                rules.add(temp1);
            });
        } else {
            // 配置了消息收敛，则需要生成两个规则，第一个规则用于计算，第二个规则用于消息收敛,并且需要生成一个内存源，用于连接两个规则,一个计算源，用于获取数据来源
            // 首先确定内存源的构建sql片段
            String converStreamSql = "CREATE STREAM streamParam () WITH (DATASOURCE=\"streamParam/result\", FORMAT=\"json\" TYPE=\"memory\");";

            //判断收敛方式，从而确定收敛规则的sql片段
            String converSql = "SELECT * FROM streamParam GROUP BY TUMBLINGWINDOW(" + warningSaveVO.getActionTimeType() + "," + warningSaveVO.getActionTimeValue()
                    + ") HAVING ( count(*) >= " + warningSaveVO.getActionProcessValue() + " )";
            if (warningSaveVO.getActionProcessValue() > 1) {
                converSql = converSql.replace("SELECT *", "SELECT last_value(*, true)");
            }

            String finalConverSql = converSql;
            warningSaveVO.getEquipmentList().forEach(equipment -> {
                // 创建内存源    命名规则 scene_ruleId_设备编码
                String streamParam = "scene_" + ruleId + "_" + equipment + "_step2";
                JSONObject converStream = new JSONObject();
                converStream.put("sql", converStreamSql.replace("streamParam", streamParam));
                JSONObject temp0 = new JSONObject();
                temp0.put("type", "streamCreation");
                temp0.put("data", converStream);
                temp0.put("id", streamParam);
                rules.add(temp0);
                // 创建收敛规则
                JSONObject converObject = new JSONObject();
                //第一个规则id格式规定为   scene-场景记录ruleId(前面已通过代码生成)-设备编码-step2
                converObject.put("id", "scene-" + ruleId + "-" + equipment + "-step2");
                //规则名称格式规定为  场景-场景名称-设备编码-step2
                converObject.put("name", "场景-" + warningSaveVO.getWarningName() + "-" + equipment + "-step2");
                converObject.put("sql", finalConverSql.replace("streamParam", streamParam));
                settingsMQTTAction(converObject, eventTopic);
                JSONObject temp1 = new JSONObject();
                temp1.put("type", "ruleCreation");
                temp1.put("data", converObject);
                temp1.put("id", converObject.getString("id"));
                rules.add(temp1);
                //创建计算规则
                JSONObject jsonObject = new JSONObject();
                //第一个规则id格式规定为   scene-场景记录ruleId(前面已通过代码生成)-设备编码-step1
                jsonObject.put("id", "scene-" + ruleId + "-" + equipment + "-step1");
                //规则名称格式规定为  场景-场景名称-设备编码-step1
                jsonObject.put("name", "场景-" + warningSaveVO.getWarningName() + "-" + equipment + "-step1");
                String equipmentCode = "'" + equipment + "'";
                String equipmentName = "'" + equipmentCodeNameMap.get(equipment) + "'";
                jsonObject.put("sql", sql.replace("rabbit_origin", "EventPush_" + equipment).replace("deviceParam", equipment).replace("equipmentParam", equipmentCode).replace("nameParam", equipmentName));
                settingsMemoryAction(jsonObject, streamParam + "/result");
                JSONObject temp2 = new JSONObject();
                temp2.put("type", "ruleCreation");
                temp2.put("data", jsonObject);
                temp2.put("id", jsonObject.getString("id"));
                rules.add(temp2);
            });
        }
        //设置该场景对应的计算引擎规则id
        warningSaveVO.setRuleId(ruleId);
        //将条件配置列表使用json文本保存
        warningSaveVO.setRuleExpression(JSONArray.toJSONString(warningSaveVO.getWhereList()));
        //将需要生成计算规则的信息使用json文本保存，场景启动时需要遍历执行
        warningSaveVO.setRuleSql(JSONArray.toJSONString(rules));
        //将场景关联的设备编码列表保存
        warningSaveVO.setEquipmentCode(String.join(",", warningSaveVO.getEquipmentList()));
        return Boolean.TRUE;
    }

    /**
     * 根据配置生成规则sql，创建流计算规则，组装业务持久化数据
     * 注意！！！！！！！！  场景保存更新时只根据最新配置构建规则或内存源所需数据，场景启动时再调用计算引擎生成规则或内存源。
     * 此处规则构建有三种情况，1：普通条件 2：窗口条件 3：设备事件，
     * 1和2都是针对设备属性的，所以构建逻辑放在一起，3是针对设备事件单独处理，包括连接rabbit的配置组也用独有的
     * @param warningSaveVO
     * @return
     */
    private boolean processWarningRule(WarningSaveVO warningSaveVO) {
        //调用远程接口，获取设备信息
        List<EquipmentDTO> equipmentDTOList = equipmentApi.getEquipmentCode(warningSaveVO.getEquipmentList());
        if (ObjectUtils.isEmpty(equipmentDTOList)) {
            return false;
        }
        Map<String, String> equipmentTopic = equipmentDTOList.stream().collect(Collectors.toMap(EquipmentDTO::getEquipmentCode, EquipmentDTO::getEventPush));
        Map<String, String> equipmentCodeNameMap = equipmentDTOList.stream().collect(Collectors.toMap(EquipmentDTO::getEquipmentCode, EquipmentDTO::getEquipmentName));

        //如果是设备事件类型的，在此处特殊处理
        if ("3".equals(warningSaveVO.getWhereType())) {
            //处理设备事件类型的条件场景
            return processWarningRuleEvent(warningSaveVO, equipmentTopic, equipmentCodeNameMap);
        }
        StringBuffer sb = new StringBuffer();
        List<String> selectList = new ArrayList<>();
        //拼接过滤条件
        warningSaveVO.getWhereList().forEach(item -> {
            // and or 拼接
            sb.append(item.getWhereType());
            // 聚合函数拼接
            sb.append(computeType(item, selectList));
            // 条件符号拼接
            sb.append(symbolType(item));
        });

        // 处理流计算规则sql
        String whereSql;
        if ("1".equals(warningSaveVO.getWhereType())) {
            // 如果是普通条件
            whereSql = " WHERE (" + sb + ") AND (deviceCode = 'deviceParam')"
                    + " and substring(thetime, 11, 19) between " + "'" + warningSaveVO.getBeginTime() + "'" + " and " + "'" + warningSaveVO.getEndTime() + "'";
        } else {
            // 如果是时间窗口
            whereSql = " GROUP BY TUMBLINGWINDOW(" + warningSaveVO.getTimeType()
                    + "," + warningSaveVO.getTimeRange() + ") " + "FILTER (where deviceCode = 'deviceParam' "
                    + " and substring(thetime, 11, 19) between " + "'" + warningSaveVO.getBeginTime() + "'" + " and " + "'" + warningSaveVO.getEndTime() + "'" +")"
                    + " HAVING (" + sb + ")";
        }

        //查询字段列表增加时间字段
        selectList.add("thetime AS thetime");
        // 增加设备信息字段
        selectList.add("equipmentParam AS deviceCode");
        selectList.add("nameParam AS deviceName");
        //生成一个唯一id，用于标识这个规则sql属于哪条场景配置，在后续动作执行时使用
        String ruleId;
        if (StringUtils.isNotBlank(warningSaveVO.getRuleId())) {
            ruleId = warningSaveVO.getRuleId();
        } else {
            ruleId = generateUniqueId();
        }
        selectList.add(ruleId + " ");
        //凭借固定sql片段，如select、from、以及select字段列表
        String sql = startSql + " " + String.join(", ", selectList) + ruleSql + whereSql;
        //声明计算规则列表，用于存储需要调用计算引擎生成的规则或数据源
        JSONArray rules = new JSONArray();
        // 处理消息收敛配置,主要是将计算引擎中原本一条规则记录，拆分成两个规则，两个规则之间使用引擎的内存主题进行连接
        if ("0".equals(warningSaveVO.getActionProcessType())) {
            //未配置消息收敛则，正常生成规则即可
            // 遍历适用设备列表，为每个设备进行流计算引擎接口调用，生成该场景的流计算规则。计算源使用设备的属性上报源，此源在设备启动时已创建
            warningSaveVO.getEquipmentList().forEach(equipment -> {
                //创建计算规则
                JSONObject jsonObject = new JSONObject();
                //todo 设置输出动作，后续考虑用户自主可配
                settingsMQTTAction(jsonObject, attributeTopic);
                //规则id格式规定为   scene-场景记录ruleId(前面已通过代码生成)-设备编码
                jsonObject.put("id", "scene-" + ruleId + "-" + equipment);
                //规则名称格式规定为  场景-场景名称-设备编码
                jsonObject.put("name", "场景-" + warningSaveVO.getWarningName() + "-" + equipment);
                String equipmentCode = "'" + equipment + "'";
                String equipmentName = "'" + equipmentCodeNameMap.get(equipment) + "'";
                jsonObject.put("sql", sql.replace("rabbit_origin", "AttributePush_" + equipment).replace("deviceParam", equipment).replace("equipmentParam", equipmentCode).replace("nameParam", equipmentName));
                JSONObject temp1 = new JSONObject();
                temp1.put("type", "ruleCreation");
                temp1.put("data", jsonObject);
                temp1.put("id", jsonObject.getString("id"));
                rules.add(temp1);
            });
        } else {
            // 配置了消息收敛，则需要生成两个规则，第一个规则用于计算，第二个规则用于消息收敛,并且需要生成一个内存源，用于连接两个规则,一个计算源，用于获取数据来源
            // 首先确定内存源的构建sql片段
            String converStreamSql = "CREATE STREAM streamParam () WITH (DATASOURCE=\"streamParam/result\", FORMAT=\"json\" TYPE=\"memory\");";

            //判断收敛方式，从而确定收敛规则的sql片段
            String converSql = "SELECT * FROM streamParam GROUP BY TUMBLINGWINDOW(" + warningSaveVO.getActionTimeType() + "," + warningSaveVO.getActionTimeValue()
                            + ") HAVING ( count(*) >= " + warningSaveVO.getActionProcessValue() + " )";
            if (warningSaveVO.getActionProcessValue() > 1) {
                converSql = converSql.replace("SELECT *", "SELECT last_value(*, true)");
            }

            String finalConverSql = converSql;
            warningSaveVO.getEquipmentList().forEach(equipment -> {
                // 创建内存源    命名规则 scene_ruleId_设备编码
                String streamParam = "scene_" + ruleId + "_" + equipment + "_step2";
                JSONObject converStream = new JSONObject();
                converStream.put("sql", converStreamSql.replace("streamParam", streamParam));
                JSONObject temp0 = new JSONObject();
                temp0.put("type", "streamCreation");
                temp0.put("data", converStream);
                temp0.put("id", streamParam);
                rules.add(temp0);
                // 创建收敛规则
                JSONObject converObject = new JSONObject();
                //第一个规则id格式规定为   scene-场景记录ruleId(前面已通过代码生成)-设备编码-step2
                converObject.put("id", "scene-" + ruleId + "-" + equipment + "-step2");
                //规则名称格式规定为  场景-场景名称-设备编码-step2
                converObject.put("name", "场景-" + warningSaveVO.getWarningName() + "-" + equipment + "-step2");
                converObject.put("sql", finalConverSql.replace("streamParam", streamParam));
                settingsMQTTAction(converObject, attributeTopic);
                JSONObject temp1 = new JSONObject();
                temp1.put("type", "ruleCreation");
                temp1.put("data", converObject);
                temp1.put("id", converObject.getString("id"));
                rules.add(temp1);
                //创建计算规则
                JSONObject jsonObject = new JSONObject();
                //第一个规则id格式规定为   scene-场景记录ruleId(前面已通过代码生成)-设备编码-step1
                jsonObject.put("id", "scene-" + ruleId + "-" + equipment + "-step1");
                //规则名称格式规定为  场景-场景名称-设备编码-step1
                jsonObject.put("name", "场景-" + warningSaveVO.getWarningName() + "-" + equipment + "-step1");
                String equipmentCode = "'" + equipment + "'";
                String equipmentName = "'" + equipmentCodeNameMap.get(equipment) + "'";
                jsonObject.put("sql", sql.replace("rabbit_origin", "AttributePush_" + equipment).replace("deviceParam", equipment).replace("equipmentParam", equipmentCode).replace("nameParam", equipmentName));
                settingsMemoryAction(jsonObject, streamParam + "/result");
                JSONObject temp2 = new JSONObject();
                temp2.put("type", "ruleCreation");
                temp2.put("data", jsonObject);
                temp2.put("id", jsonObject.getString("id"));
                rules.add(temp2);
            });
        }
        //设置该场景对应的计算引擎规则id
        warningSaveVO.setRuleId(ruleId);
        //将条件配置列表使用json文本保存
        warningSaveVO.setRuleExpression(JSONArray.toJSONString(warningSaveVO.getWhereList()));
        //将需要生成计算规则的信息使用json文本保存，场景启动时需要遍历执行
        warningSaveVO.setRuleSql(JSONArray.toJSONString(rules));
        //将场景关联的设备编码列表保存
        warningSaveVO.setEquipmentCode(String.join(",", warningSaveVO.getEquipmentList()));
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteWarningConfig(Long id) {
        WarningDO warningDO = getById(id);
        if (warningDO.getIsEnable() == 1) {
            //如果场景已启用,则先停掉该场景,移除相关计算规则
            JSONArray rules = JSONObject.parseArray(warningDO.getRuleSql());
            if (ObjectUtils.isNotEmpty(rules)) {
                // 场景停用
                rules.forEach(e -> {
                    JSONObject rule = (JSONObject) e;
                    switch (rule.getString("type")) {
                        case "ruleCreation":
                            WarningClient.ruleDelete(rule.getString("id"), url);
                            break;
                        case "streamCreation":
                            WarningClient.streamDelete(rule.getString("id"), url);
                            break;
                        default:
                            break;
                    }
                });
            }
        }

        configActionService.deleteByConfigId(id);
        return removeById(id);
    }

    /**
     * 场景启用时遍历关联设备，针对每个设备进行规则和内存源的创建
     * 场景停用时遍历关联设备，针对每个设备进行规则和内存源的删除
     *
     * @param warningDO
     * @return
     */
    @SneakyThrows
    @Override
    public CommonResult<WarningDO> updateStatus(WarningDO warningDO) {
        WarningDO warning = getById(warningDO.getId());

        //如果场景状态有变化或者是手动执行场景
        if (warningDO.getIsEnable() != warning.getIsEnable() || "3".equals(warning.getWarningType())) {
            // 1：条件场景 2：定时场景 3：手动场景
            switch (warning.getWarningType()){
                //条件场景
                case "1":
                    try {
                        JSONArray rules = JSONObject.parseArray(warning.getRuleSql());
                        //计算引擎规定 一个计算规则的创建必须是  先有源再有使用这个源的规则，删除计算规则时，先删除规则，再删除源
                        if (ObjectUtils.isNotEmpty(rules)) {
                            if (warningDO.getIsEnable() == 1) {
                                List<JSONObject> ruleList = new ArrayList<>();
                                // 场景启用
                                rules.forEach(e -> {
                                    JSONObject rule = (JSONObject) e;
                                    switch (rule.getString("type")) {
                                        // 如果是规则，则先记录，待源创建完成再调用api创建
                                        case "ruleCreation":
                                            ruleList.add(rule.getJSONObject("data"));
                                            break;
                                        // 如果是源，则直接调用api创建
                                        case "streamCreation":
                                            WarningClient.streamCreation(rule.getJSONObject("data"), url);
                                            break;
                                        default:
                                            break;
                                    }
                                });
                                ruleList.forEach(e -> {
                                    WarningClient.ruleCreation(e, url);
                                });

                            } else {
                                List<String> streamList = new ArrayList<>();
                                // 场景停用
                                rules.forEach(e -> {
                                    JSONObject rule = (JSONObject) e;
                                    switch (rule.getString("type")) {
                                        // 如果是规则，则调用api删除
                                        case "ruleCreation":
                                            WarningClient.ruleDelete(rule.getString("id"), url);
                                            break;
                                        // 如果是源，则先记录，待规则删除完成再调用api删除
                                        case "streamCreation":
                                            streamList.add(rule.getString("id"));
                                            break;
                                        default:
                                            break;
                                    }
                                });
                                streamList.forEach(e -> {
                                    WarningClient.streamDelete(e, url);
                                });
                            }
                        }
                    } catch (Exception e) {
                        return CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), (warningDO.getIsEnable() == 1 ? "启用" : "停用") + "异常");
                    }
                    //更新状态数据
                    return lambdaUpdate().set(WarningDO::getIsEnable, warningDO.getIsEnable())
                            .eq(WarningDO::getId, warningDO.getId())
                            .update() ? CommonResult.success(warningDO) : CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), "操作失败");
                //定时场景
                case "2":
                    int jobId = warning.getJobId();
                    //场景启用
                    if (warningDO.getIsEnable() == 1) {
                        //定时任务创建或更新
                        XxlJobEntity xxlJobEntity = new XxlJobEntity();
                        xxlJobEntity.setCron(warning.getCron()); //定时规则cron
                        xxlJobEntity.setExecutorFailRetryCount(warning.getExecutorFailRetryCount()); //定时规则cron
                        xxlJobEntity.setAppName(this.appName); //定时规则cron
                        xxlJobEntity.setExecutorHandler(this.executorHandler); //定时规则cron
                        xxlJobEntity.setCron(warning.getCron()); //定时规则cron
                        xxlJobEntity.setJobDesc(warning.getWarningName()); //任务描述
                        xxlJobEntity.setJobParam(warning.getId().toString()); //任务参数
                        xxlJobEntity.setId(jobId); //任务id
                        xxlJobEntity.setJobType(this.jobType); //代表该定时任务是场景类型
                        xxlJobEntity.setRelevanceId(warning.getId()); //场景的记录id
                        //创建或更新场景对应的定时任务
                        jobId = Integer.parseInt(xxljobAdminApi.saveOrUpdate(xxlJobEntity).getData());
                        //启动定时任务
                        xxljobAdminApi.start(jobId);
                    } else {
                        //场景停用，如果关联定时任务id有值，则需要停止对应的定时任务
                        if (jobId > 0) {
                            //暂停定时任务
                            xxljobAdminApi.stop(jobId);
                        }
                    }

                    return lambdaUpdate().set(WarningDO::getIsEnable, warningDO.getIsEnable())
                            .set(WarningDO::getJobId, jobId)
                            .eq(WarningDO::getId, warningDO.getId())
                            .update() ? CommonResult.success(warningDO) : CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), "操作失败");
                //手动执行场景
                case "3":
                    //更新状态数据
                    WarningCommonVO warningCommonVO = new WarningCommonVO();
                    warningCommonVO.setWarningDetailVO(selectWarningByWarningId(warningDO.getId()));
                    //todo 这里需要构造一个json对象传入，后续考虑加入什么内容
                    warningCommonVO.setJsonObject(new JSONObject());
                    return warningHandler.handleCommonAction(warningCommonVO) ? CommonResult.success(warningDO) : CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), "操作失败");
                default:
            }
        }
        return CommonResult.success(warningDO);
    }

    /**
     * 根据告警id获取告警配置信息和动作列表，此方法仅供告警事件调用
     *
     * @param id
     * @return
     */
    @Override
    public WarningDetailVO selectWarningById(Long id) {
        WarningDetailVO warningDetailVO = new WarningDetailVO();
        WarningDO warningDO = getByRuleId(id);
        BeanUtils.copyProperties(warningDO, warningDetailVO);
        List<ConfigActionDO> actionDOList = configActionService.getActionByConfigId(warningDO.getId());
        warningDetailVO.setActionDOList(actionDOList);
        return warningDetailVO;
    }

    @Override
    public WarningDetailVO selectWarningByWarningId(Long id) {
        WarningDetailVO warningDetailVO = new WarningDetailVO();
        WarningDO warningDO = baseMapper.selectById(id);
        BeanUtils.copyProperties(warningDO, warningDetailVO);
        List<ConfigActionDO> actionDOList = configActionService.getActionByConfigId(warningDO.getId());
        warningDetailVO.setActionDOList(actionDOList);
        return warningDetailVO;
    }
    @Override
    public CommonResult<WarningSaveVO> getWarningById(Long id) {
        WarningSaveVO warningSaveVO = new WarningSaveVO();
        WarningDO warningDO = getById(id);
        BeanUtils.copyProperties(warningDO, warningSaveVO);
        List<ConfigActionDO> actionDOList = configActionService.getActionByConfigId(warningDO.getId());
        warningSaveVO.setActionDOList(actionDOList);
        warningSaveVO.setWhereList(JSONObject.parseArray(warningDO.getRuleExpression(), ConfigRule.class));
        if(ObjectUtils.isNotEmpty(warningDO.getEquipmentCode())){
            // 根据设备编码查询设备数据
            List<String> list = Arrays.asList(warningDO.getEquipmentCode().split(","));
            List<EquipmentVO> equipmentNames = baseMapper.getEquipmentNameByCodes(list);
            warningSaveVO.setEquipmentList(Arrays.asList(warningDO.getEquipmentCode().split(",")));
            warningSaveVO.setEquipmentName(equipmentNames);
        }
        return CommonResult.success(warningSaveVO);
    }

    @Override
    public CommonResult<Boolean> restart(Long id) {
        WarningDO warning = getById(id);
        if (warning.getIsEnable() == 1) {
            switch (warning.getWarningType()) {
                case "1":
                    //如果场景已启用，则进行表明计算引擎内存在该场景的规则配置，可以进行重启
                    JSONArray rules = JSONObject.parseArray(warning.getRuleSql());
                    if (ObjectUtils.isNotEmpty(rules)) {
                        rules.forEach(e -> {
                            JSONObject rule = (JSONObject) e;
                            switch (rule.getString("type")) {
                                case "ruleCreation":
                                    WarningClient.ruleRestart(rule.getString("id"), url);
                                    break;
                                default:
                                    break;
                            }
                        });
                    }
                    break;
                case "2":
                    xxljobAdminApi.start(warning.getJobId());
                    break;
                case "3":
                    // TODO: 2024/10/25 手动场景无重启业务
                    break;
                default:
                    break;
            }
            return CommonResult.success(null);
        }
        return CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), "场景未启用，无需重启");
    }

    /**
     * 设置mqtt动作
     *
     * @param jsonObject
     */
    private void settingsMQTTAction(JSONObject jsonObject, String actionsTopic) {
        JSONArray actionsArray = new JSONArray();
        JSONObject mqttAction = new JSONObject();
        JSONObject mqttObject = new JSONObject();
        mqttObject.put("connectionSelector", connectionSelector);
        mqttObject.put("server", actionsServer);
        mqttObject.put("topic", actionsTopic);
        if (actionsUsername.isEmpty()) {
            mqttObject.put("username", "");
        } else {
            mqttObject.put("username", actionsUsername);
        }
        if (actionsPassword.isEmpty()) {
            mqttObject.put("password", "");
        } else {
            mqttObject.put("password", actionsPassword);
        }
        mqttObject.put("sendSingle", sendSingle);
        mqttAction.put("mqtt", mqttObject);
        actionsArray.add(mqttAction);
        jsonObject.put("actions", actionsArray);
    }

    /**
     * 设置内存动作
     *
     * @param jsonObject
     */
    private void settingsMemoryAction(JSONObject jsonObject, String topic) {
        JSONArray actionsArray = new JSONArray();
        JSONObject memoryAction = new JSONObject();
        JSONObject memoryObject = new JSONObject();
        memoryObject.put("topic", topic);
        memoryAction.put("memory", memoryObject);
        actionsArray.add(memoryAction);
        jsonObject.put("actions", actionsArray);
    }

    public static String generateUniqueId() {
        long timestamp = System.currentTimeMillis();
        int randomNumber = (int) (Math.random() * 1000);
        int uniqueId = counter.getAndIncrement();

        String id = String.format("%d%03d%04d", timestamp, randomNumber, uniqueId);

        if (id.length() > 15) {
            id = id.substring(0, 15);
        } else if (id.length() < 15) {
            id = String.format("%-15s", id).replace(' ', '0');
        }
        return id;
    }

    @Override
    public WarningDO getByRuleId(Long id) {
        return baseMapper.getByRuleId(id);
    }

    @Override
    public List<ConfigCountVO> getWarningCountByEquipmentId(List<String> equipmentCodeList) {
        List<WarningDO> warningDOS = baseMapper.getWarningCountByEquipmentId(equipmentCodeList);
        // 根据场景列表和设备编码列表构建数据
        List<ConfigCountVO> countVOS = new ArrayList<>();
        equipmentCodeList.forEach(item -> {
            Integer equipmentCount = 0;
            ConfigCountVO configCountVO = new ConfigCountVO();
            configCountVO.setEquipmentCode(item);
            // 计算每个设备关联场景数
            for (WarningDO warning : warningDOS) {
                List<String> list = Arrays.asList(warning.getEquipmentCode().split(","));
                if (list.contains(item)) {
                    equipmentCount++;
                }
            }
            configCountVO.setWarningCount(equipmentCount);
            countVOS.add(configCountVO);
        });
        return countVOS;
    }

    @Override
    public Integer getConfifCount() {
        return baseMapper.getConfifCount();
    }

    @Override
    public List<EquipmentRelateWarningVO> getEquipmentRelateWarning() {
        return baseMapper.getEquipmentRelateWarning();
    }

    @Override
    public boolean deleteByEquipmentId(Long id) {
        // 根据设备id查询出所有关联的告警信息
        List<WarningDO> warningDOList = baseMapper.selectByEquipmentId(id);
        boolean status = deleteBatchConfig(warningDOList);
        return status;
    }

    @Override
    public List<WarningConfigStatusVO> getStatusCount() {
        return baseMapper.getStatusCount();
    }

    @Override
    public Boolean untie(Long id, String equipmentCode) {
        // 查询对应的场景信息
        WarningDO warningDO = getById(id);
        String equipmentCodes = warningDO.getEquipmentCode();
        if (equipmentCodes.isEmpty()) {
            return false;
        }
        List<String> equipmentCodeList = Arrays.asList(equipmentCodes.split(","));
        // 移除当前设备（后续需要将告警平台关于此设备的配置数据同步解绑更新，这里只是完成了系统平台的业务操作；）
        //如果该场景启用  则删除当前设备的规则  并且需要将ruleSql中关于此设备的数据删除
        equipmentCodeList = equipmentCodeList.stream().filter(e -> !e.equals(equipmentCode)).collect(Collectors.toList());
        warningDO.setEquipmentCode(String.join(",", equipmentCodeList));
        //定时任务 没有规则  直接修改  后续定时任务不再关联设备
        if("1".equals(warningDO.getWarningType())){
            String s = untieDeleteRule(warningDO, equipmentCode);
            warningDO.setRuleSql(s);
        }

        return saveOrUpdate(warningDO);
    }

    @Override
    public CommonResult<Boolean> equipmentUntie(String equipmentCode) {
        // 根据设备编码查询场景数据
        List<WarningDO> warningDOList = baseMapper.getWarningByEquipmentCode(equipmentCode);
        for (WarningDO warningDO : warningDOList) {
            String equipmentCodes = warningDO.getEquipmentCode();
            if (equipmentCodes.isEmpty()) {
                return CommonResult.success(false);
            }
            List<String> equipmentCodeList = Arrays.asList(equipmentCodes.split(","));
            // 移除当前设备（后续需要将告警平台关于此设备的配置数据同步解绑更新，这里只是完成了系统平台的业务操作；）
            //如果该场景启用  则删除当前设备的规则  并且需要将ruleSql中关于此设备的数据删除
            equipmentCodeList = equipmentCodeList.stream().filter(e -> !e.equals(equipmentCode)).collect(Collectors.toList());
            warningDO.setEquipmentCode(String.join(",", equipmentCodeList));
            //定时任务 没有规则  直接修改  后续定时任务不再关联设备
            if("1".equals(warningDO.getWarningType())){
                String s = untieDeleteRule(warningDO, equipmentCode);
                warningDO.setRuleSql(s);
            }
        }
        saveOrUpdateBatch(warningDOList);
        return CommonResult.success(true);
    }

    public String untieDeleteRule(WarningDO warningDO, String equipmentCode){
        String ruleKey = "scene-" + warningDO.getRuleId() + "-" + equipmentCode;
        JSONArray rules = JSONObject.parseArray(warningDO.getRuleSql());
        List<String> ruleKeyStreams = new ArrayList<>();
        //0不启用  1启用
        if (ObjectUtils.isNotEmpty(rules)) {
            // 场景停用
            for (int i = 0; i < rules.size(); i++) {
                JSONObject rule = (JSONObject) rules.get(i);
                //如果是规则  并且是需要删除的规则 则删除
                if ("ruleCreation".equals(rule.getString("type")) && rule.getString("id").contains(ruleKey)){
                    if (warningDO.getIsEnable() == 1){
                        WarningClient.ruleDelete(rule.getString("id"), url);
                    }
                    rules.remove(i);
                    i--;
                }
                //源 删完规则后删除
                if ("streamCreation".equals(rule.getString("type")) && rule.getString("id").contains(ruleKey.replace("-","_"))){
                    if (warningDO.getIsEnable() == 1){
                        ruleKeyStreams.add(rule.getString("id"));
                    }
                    rules.remove(i);
                    i--;
                }
            }
            //删除源
            ruleKeyStreams.stream().forEach(rule ->{
                WarningClient.streamDelete(rule, url);
            });
        }
        return rules.toJSONString();
    }

    public boolean deleteBatchConfig(List<WarningDO> warning) {
        WarningClient warningClient = new WarningClient();
        boolean isSuccess = false;
        boolean status = true;
        for (WarningDO warningDO : warning) {
            isSuccess = warningClient.ruleDelete(warningDO.getRuleId(), url);
            if (isSuccess) {
                removeById(warningDO.getId());
                configActionService.deleteByConfigId(warningDO.getId());
                status = true;
            } else {
                status = false;
            }
        }
        return status;
    }

    /**
     * 根据条件参数拼接相关条件符号和值
     *
     * @param configRule
     * @return
     */
    public String symbolType(ConfigRule configRule) {
        if(ObjectUtils.isEmpty(configRule.getConditionType())){
            return "";
        }
        switch (configRule.getConditionType()) {
            case "1":
                return "= " + configRule.getParam1() + " ";
            case "2":
                return "!= " + configRule.getParam1() + " ";
            case "3":
                return "> " + configRule.getParam1() + " ";
            case "4":
                return ">= " + configRule.getParam1() + " ";
            case "5":
                return "< " + configRule.getParam1() + " ";
            case "6":
                return "<= " + configRule.getParam1() + " ";
            case "7":
                return "BETWEEN " + configRule.getParam1() + " AND " + configRule.getParam2() + " ";
            case "8":
                return "NOT BETWEEN " + configRule.getParam1() + " AND " + configRule.getParam2() + " ";
            case "9":
                return "IN (" + configRule.getParam1() + ") ";
            case "10":
                return "NOT IN (" + configRule.getParam1() + ") ";
            default:
                return "";
        }
    }

    /**
     * 拼接聚合函数sql片段
     *
     * @param configRule
     * @return
     */
    public String computeType(ConfigRule configRule, List<String> selectList) {
        switch (configRule.getValueType()) {
            case "1":
                selectList.add(configRule.getPointCode() + " AS " + configRule.getPointCode());
                return " " + configRule.getPointCode() + " ";
            case "2":
                selectList.add(" lag(" + configRule.getPointCode() + ") AS lag_" + configRule.getPointCode());
                return " lag(" + configRule.getPointCode() + ") " + "OVER (PARTITION BY deviceCode = 'deviceParam') ";
            case "3":
                selectList.add(" avg(" + configRule.getPointCode() + ") AS avg_" + configRule.getPointCode());
                return " avg(" + configRule.getPointCode() + ") ";
            case "4":
                selectList.add(" max(" + configRule.getPointCode() + ") AS max_" + configRule.getPointCode());
                return " max(" + configRule.getPointCode() + ") ";
            case "5":
                selectList.add(" min(" + configRule.getPointCode() + ") AS min_" + configRule.getPointCode());
                return " min(" + configRule.getPointCode() + ") ";
            case "6":
                selectList.add(" sum(" + configRule.getPointCode() + ") AS sum_" + configRule.getPointCode());
                return " sum(" + configRule.getPointCode() + ") ";
            case "7":
                selectList.add(" count(" + configRule.getPointCode() + ") AS count_" + configRule.getPointCode());
                return " count(" + configRule.getPointCode() + ") ";
            case "8":
                // 当前值等于上一次值
                selectList.add(configRule.getPointCode() + " AS " + configRule.getPointCode());
                return " " + configRule.getPointCode() + " = " + " lag(" + configRule.getPointCode() + ") ";
            case "9":
                // 当前值不等于上一次值
                selectList.add(configRule.getPointCode() + " AS " + configRule.getPointCode());
                return " " + configRule.getPointCode() + " != " + " lag(" + configRule.getPointCode() + ") ";
            case "10":
                // 当前值小于上一次值
                selectList.add(configRule.getPointCode() + " AS " + configRule.getPointCode());
                return " " + configRule.getPointCode() + " < " + " lag(" + configRule.getPointCode() + ") ";
            case "11":
                // 当前值大于上一次值
                selectList.add(configRule.getPointCode() + " AS " + configRule.getPointCode());
                return " " + configRule.getPointCode() + " > " + " lag(" + configRule.getPointCode() + ") ";
            default:
                return "";
        }
    }

    /**
     * 传入两个数求并向下取整
     */
    public static double floorDivision(double numerator, double denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("分母不可为零");
        }
        return Math.floor(numerator / denominator);
    }
}
