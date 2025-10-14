package cn.snowsoft.iot.module.warning.service.warning.record;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.message.api.messageRecord.MessageRecordApi;
import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.RecordVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.WarningEquipmentVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailReqVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailRespVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordPageVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordStatusVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.WarningDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.NorthDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningActionRecord;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningRecordDO;
import cn.snowsoft.iot.module.warning.dal.mysql.warning.record.WarningRecordMapper;
import cn.snowsoft.iot.module.warning.enums.WarningRecord;
import cn.snowsoft.iot.module.warning.service.warning.config.IWarningService;
import cn.snowsoft.iot.module.warning.service.warning.north.INorthService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WarningRecordServiceImpl extends ServiceImpl<WarningRecordMapper, WarningRecordDO> implements IWarningRecordService {

    @Resource
    private IWarningService warningService;

    @Resource
    private MessageRecordApi messageRecordApi;
    @Resource
    private IWarningActionRecordService warningActionRecordService;

    @Resource
    private INorthService actionService;


    @Override
    public IPage<WarningRecordDO> selectRecordPage(WarningRecordPageVO recordPageVO) {
        IPage<WarningRecordDO> page = new Page<>(recordPageVO.getPageNo(), recordPageVO.getPageSize());
        return baseMapper.selectWarningPage(page, recordPageVO);
    }

    @Override
    public CommonResult<WarningRecordDO> updateStatus(WarningRecordStatusVO recordStatusVO) {
        if (recordStatusVO.getId() != null) {
            WarningRecordDO warningRecordDO = new WarningRecordDO();
            warningRecordDO.setId(recordStatusVO.getId());
            if (recordStatusVO.getStatus() != null) {
                warningRecordDO.setStatus(recordStatusVO.getStatus());
                warningRecordDO.setHandleUser(recordStatusVO.getHandleUser());
                LocalDateTime now = LocalDateTime.now();
                warningRecordDO.setHandleTime(now);
                warningRecordDO.setRemark(recordStatusVO.getRemark());
                updateById(warningRecordDO);
                return CommonResult.success(warningRecordDO);
            } else {
                return CommonResult.error(400, "参数错误");
            }
        } else {
            return CommonResult.error(400, "参数错误");
        }
    }

    /**
     * 条件场景持久化mysql调用方法
     * @param jsonObject
     * 告警发生时，查询同场景同设备未处理的告警记录信息，如果未查到则新增当前告警，如果存在则更新即可
     * @return
     */
    public boolean handleWarningRecord(JSONObject jsonObject, Long id, String warningName) {
        String warningTime = jsonObject.getString("thetime");
        String equipmentName = jsonObject.getString("deviceName");
        String equipmentCode = jsonObject.getString("deviceCode");
        String uuId = jsonObject.getString("uuId");
        //查询场景记录 同设备、同场景、未处理
        WarningRecordDO temp = lambdaQuery().eq(WarningRecordDO::getConfigId, id)
                .eq(WarningRecordDO::getEquipmentCode, equipmentCode)
                .eq(WarningRecordDO::getStatus, 0)
                .orderByDesc(WarningRecordDO::getCreateTime)
                .one();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        //如果为空 说明可能为定时任务或手动
        if (ObjectUtil.isEmpty(warningTime)) {
            warningTime = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss.SSS");
        }
        if (!warningTime.matches(".*\\.\\d{1,3}$")) {
            warningTime += ".000";
        }
        LocalDateTime dateTime = LocalDateTime.parse(warningTime, formatter);

        if (ObjectUtil.isEmpty(temp)) {
            //新增告警记录
            WarningRecordDO warningRecordDO = new WarningRecordDO();
            //设置uuId,用于关联此次告警记录对应的动作执行记录
            warningRecordDO.setWarningTag(uuId);
            warningRecordDO.setConfigId(id);
            warningRecordDO.setConfigName(warningName);
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);
            warningRecordDO.setWarningData(jsonArray.toJSONString());
            // 设备信息
            warningRecordDO.setEquipmentName(equipmentName);
            warningRecordDO.setEquipmentCode(equipmentCode);
            warningRecordDO.setWarningTime(dateTime);
            warningRecordDO.setStatus(WarningRecord.DISABLE);
            warningRecordDO.setWarningType("1");
            save(warningRecordDO);
            return true;
        } else {
            //更新告警记录
            //追加uuId,用于关联此次告警记录对应的动作执行记录
            temp.setWarningTag(temp.getWarningTag() + "," + uuId);
            //设置告警数据
            JSONArray jsonArray = JSON.parseArray(temp.getWarningData());
            jsonArray.add(jsonObject);
            temp.setWarningData(jsonArray.toJSONString());
            //设备告警时间为本次告警时间
            temp.setWarningTime(dateTime);
            updateById(temp);
            return false;
        }
    }

    /**
     * 定时和条件场景事件持久化mysql监听器调用方法
     * @param jsonObject
     * @param warningType 场景类型，1.条件 2.定时 3.手动
     * @return
     */
    public void handleWarningRecord(JSONObject jsonObject, String warningType, String warningName) {
        WarningRecordDO warningRecordDO = new WarningRecordDO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        Long configId = jsonObject.getLong("warningId");
        String warningTime = jsonObject.getString("thetime");
        String equipmentName = jsonObject.getString("deviceName");
        String equipmentCode = jsonObject.getString("deviceCode");
        String uuId = jsonObject.getString("uuId");
        //如果为空 说明可能为定时任务或手动
        if (ObjectUtil.isEmpty(warningTime)) {
            warningTime = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss.SSS");
        }
        if (!warningTime.matches(".*\\.\\d{1,3}$")) {
            warningTime += ".000";
        }
        LocalDateTime dateTime = LocalDateTime.parse(warningTime, formatter);
        //设置uuId,用于关联此次告警记录对应的动作执行记录
        warningRecordDO.setWarningTag(uuId);
        warningRecordDO.setConfigId(configId);
        warningRecordDO.setConfigName(warningName);
        warningRecordDO.setWarningData(String.valueOf(jsonObject));
        // 设备信息
        warningRecordDO.setEquipmentName(equipmentName);
        warningRecordDO.setEquipmentCode(equipmentCode);
        warningRecordDO.setWarningTime(dateTime);
        warningRecordDO.setStatus(WarningRecord.DISABLE);
        warningRecordDO.setWarningType(warningType);
        save(warningRecordDO);
    }

    @Override
    public CommonResult<WarningRecordDO> insertRecord(JSONObject jsonObject) {
        WarningRecordDO warningRecordDO = new WarningRecordDO();
//        JSONObject jsonObject =  JSON.parseObject(data);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        Long configId = (Long) jsonObject.get("warningId");
        String warningTime = (String) jsonObject.get("thetime");
        WarningDO warningDO = warningService.getByRuleId(configId);

        LocalDateTime dateTime = LocalDateTime.parse(warningTime, formatter);
//        String pointCode = warningDO.getPointCode();
//        Object value = jsonObject.get(pointCode);
//        String pointValue = String.valueOf(value);


//        warningRecordDO.setPointValue(pointValue);
        warningRecordDO.setConfigId(warningDO.getId());
        warningRecordDO.setConfigName(warningDO.getWarningName());
        warningRecordDO.setWarningData(String.valueOf(jsonObject));
        // 设备信息
//        warningRecordDO.setEquipmentId(warningDO.getEquipmentId());
//        warningRecordDO.setEquipmentName(warningDO.getEquipmentName());
//        // 点位信息
//        warningRecordDO.setPointId(warningDO.getPointId());
//        warningRecordDO.setPointName(warningDO.getPointName());
        warningRecordDO.setWarningTime(dateTime);
        warningRecordDO.setStatus(WarningRecord.DISABLE);

        save(warningRecordDO);
        return CommonResult.success(warningRecordDO);
    }

    @Override
    public List<RecordVO> getRecordList() {
        return baseMapper.getRecordList();
    }

    @Override
    public List<WarningEquipmentVO> getWarningEquipmentList() {
        return baseMapper.getWarningEquipmentList();
    }

    //查询告警记录动作详情
    @Override
    public List getRecodeActionDetailList(WarningRecordDetailReqVO warningRecordDetailReqVO) {
        //查询出对应场景的动作配置列表
        List<WarningRecordDetailRespVO> warningRecordDetailRespVO = baseMapper.getRecordActionDetailList(warningRecordDetailReqVO);
        //遍历动作列表，将每个动作的执行记录查询出来
        for (WarningRecordDetailRespVO recordDetailRespVO : warningRecordDetailRespVO) {
            //消息
            if ("1".equals(recordDetailRespVO.getActionType())){
                Integer failCount = 0;
                Integer successCount = 0;
                List messageRecordDetail = warningActionRecordService.getMessageRecordDetailList(Arrays.asList(warningRecordDetailReqVO.getWarningTag().split(",")));
                if (ObjectUtil.isNotEmpty(messageRecordDetail)) {
                    for (Object messageRecordRespDto : messageRecordDetail) {
                        MessageRecordRespDto messageRecordResp = (MessageRecordRespDto) messageRecordRespDto;
                        if (messageRecordResp.getMessageStatus() == 0) {
                            failCount++;
                        } else {
                            successCount++;
                        }
                    }
                }
                recordDetailRespVO.setSuccessCount(successCount);
                recordDetailRespVO.setFailCount(failCount);
                recordDetailRespVO.setDataList(messageRecordDetail);
            } else {
                Integer failCount = 0;
                Integer successCount = 0;
                WarningRecordDetailReqVO warningRecordReqVO = new WarningRecordDetailReqVO();
                warningRecordReqVO.setWarningTagList(Arrays.asList(warningRecordDetailReqVO.getWarningTag().split(",")));
                warningRecordReqVO.setActionType(recordDetailRespVO.getActionType());
                List list = new ArrayList();
                List<WarningActionRecord> outPutRecodeDetail = warningActionRecordService.getOutPutRecodeDetailList(warningRecordReqVO);
                if (ObjectUtil.isNotEmpty(outPutRecodeDetail)) {
                    for (WarningActionRecord warningActionRecord : outPutRecodeDetail) {
                        if (warningActionRecord.getOutputStatus() == 1) {
                            successCount++;
                        } else {
                            failCount++;
                        }
                    }
                }
                if(outPutRecodeDetail != null){
                    if (recordDetailRespVO.getActionType().equals("3")) {
                        List<Long> collect = outPutRecodeDetail.stream().map(WarningActionRecord::getActionId).distinct().collect(Collectors.toList());
                        // 去北向输出查询相关数据
                        List<NorthDO> northDOS = actionService.lambdaQuery().in(NorthDO::getId, collect).list();
                        Map<Long, String> map = northDOS.stream().collect(Collectors.toMap(NorthDO::getId, NorthDO::getRelatedParameter));
                        for (WarningActionRecord warningActionRecord : outPutRecodeDetail) {
                            String relatedParameter = map.get(warningActionRecord.getActionId());
                            warningActionRecord.setRequestType(JSONUtil.parseObj(relatedParameter).getStr("theme"));
                        }
                    }
                    list.addAll(outPutRecodeDetail);
                }
                recordDetailRespVO.setSuccessCount(successCount);
                recordDetailRespVO.setFailCount(failCount);
                recordDetailRespVO.setDataList(list);
            }
        }
        return warningRecordDetailRespVO;
    }

    public CommonResult<WarningRecordDO> test(String data) {
        WarningRecordDO warningRecordDO = new WarningRecordDO();
        JSONObject jsonObject =  JSON.parseObject(data);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        Long configId = (Long) jsonObject.get("warningId");
        String warningTime = (String) jsonObject.get("thetime");
        WarningDO warningDO = warningService.getById(configId);

        LocalDateTime dateTime = LocalDateTime.parse(warningTime, formatter);
//        String pointId = String.valueOf(warningDO.getPointId());
//        Object value = jsonObject.get(pointId);
//        String pointValue = String.valueOf(value);
//        EquipmentDTO equipmentDTO = equipmentApi.detail(warningDO.getEquipmentId());
//        EquipmentPointDTO pointDTO = pointApi.detail(warningDO.getPointId());


//        warningRecordDO.setPointValue(pointValue);

        warningRecordDO.setConfigId(warningDO.getId());
        warningRecordDO.setConfigName(warningDO.getWarningName());
        warningRecordDO.setWarningData(String.valueOf(jsonObject));
        // 设备信息
//        warningRecordDO.setEquipmentId(equipmentDTO.getId());
//        warningRecordDO.setEquipmentName(equipmentDTO.getEquipmentName());
        // 点位信息
//        warningRecordDO.setPointId(pointDTO.getId());
//        warningRecordDO.setPointName(pointDTO.getPointName());
        warningRecordDO.setWarningTime(dateTime);
        warningRecordDO.setStatus(WarningRecord.DISABLE);

        save(warningRecordDO);
        return CommonResult.success(warningRecordDO);
    }
}
