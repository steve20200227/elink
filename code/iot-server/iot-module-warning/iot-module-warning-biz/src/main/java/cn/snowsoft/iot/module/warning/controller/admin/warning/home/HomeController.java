package cn.snowsoft.iot.module.warning.controller.admin.warning.home;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.api.equipment.AdminEquipmentApi;
import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentRelateWarningDTO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.EquipmentRelateWarningVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.RecordVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.WarningConfigStatusVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.WarningEquipmentVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.home.HomeDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.WarningDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningRecordDO;
import cn.snowsoft.iot.module.warning.service.warning.config.IConfigActionService;
import cn.snowsoft.iot.module.warning.service.warning.config.IWarningService;
import cn.snowsoft.iot.module.warning.service.warning.record.IWarningRecordService;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "管理后台 - 首页")
@RestController
@RequestMapping("/warning/home")
public class HomeController {

    @Autowired
    private IWarningRecordService recordService;

    @Autowired
    private IConfigActionService configActionService;

    @Autowired
    private IWarningService warningService;

    @Autowired
    private AdminEquipmentApi equipmentApi;


    /**
     * 获取告警相关数据
     * @return
     */
    @GetMapping("/count")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取告警相关数据")
    public CommonResult<HomeDO> getCount () {
        HomeDO homeDO = new HomeDO();

        // 获取规则配置数
        Map<String, Long> map = new HashMap<>();
        List<WarningDO> warningDOS = warningService.list();

        Map<String, Long> stringLongMap = Optional.ofNullable(warningDOS).orElse(new ArrayList<>()).stream().collect(Collectors.groupingBy(WarningDO::getWarningType, Collectors.counting()));
        //进行构造数据
        //条件
        Long conditions = stringLongMap.get("1");
        //定时
        Long timing = stringLongMap.get("2");
        //手动
        Long manualOperation = stringLongMap.get("3");

        map.put("conditions", ObjectUtils.isNotEmpty(conditions)?conditions:0);
        map.put("timing", ObjectUtils.isNotEmpty(timing)?timing:0);
        map.put("manualOperation", ObjectUtils.isNotEmpty(manualOperation)?manualOperation:0);

        // 获取场景配置中设置的动作总数
        List<ConfigActionDO> configActionDOList = Optional.ofNullable(configActionService.lambdaQuery().list()).orElse(new ArrayList<>());
        // 分组统计配置的动作中各类型的动作数量
        List<JSONObject> actionType = configActionDOList.stream()
                .collect(Collectors.groupingBy(ConfigActionDO::getActionType, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("actionType", entry.getKey());
                    jsonObject.put("count", entry.getValue());
                    return jsonObject;
                })
                .collect(Collectors.toList());

        // 获取告警配置数
        Integer configCount = warningService.getConfifCount();
        Integer configEnableCount = 0;
        Integer configDisableCount = 0;
        List<WarningConfigStatusVO> configStatusList = warningService.getStatusCount();
        for (WarningConfigStatusVO warningConfigStatusVO : configStatusList) {
            if (warningConfigStatusVO.getIsEnable() == 1) {
                configEnableCount += warningConfigStatusVO.getCount();
            } else {
                configDisableCount += warningConfigStatusVO.getCount();
            }
        }


        List<WarningEquipmentVO> warningEquipmentVOS = recordService.getWarningEquipmentList();
        // 获取最近7天每天的告警记录总数
        List<String> keyList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        Map<LocalDate, Integer> dailyAlertCount = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDate startDate = now.minusDays(14).toLocalDate();
        LocalDateTime createTime = LocalDateTime.of(startDate, LocalTime.MIN);
        // 获取告警记录数据
        List<WarningRecordDO> recordDOS = recordService.lambdaQuery().ge(WarningRecordDO::getCreateTime, createTime).list();
        // 获取7天内的时间列表
        for (LocalDate date = startDate; !date.isAfter(now.toLocalDate()); date = date.plusDays(1)) {
            dailyAlertCount.put(date, 0); // 初始化每天的告警数为0
        }

        // 遍历得到相关数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        for (WarningRecordDO record : recordDOS) {
            LocalDate alertDate = record.getCreateTime().toLocalDate();
            if (dailyAlertCount.containsKey(alertDate)) {
                dailyAlertCount.put(alertDate, dailyAlertCount.get(alertDate) + 1);
            }
        }


        List<Map.Entry<LocalDate, Integer>> sortedEntries = new ArrayList<>(dailyAlertCount.entrySet());
        sortedEntries.sort(Comparator.comparing(Map.Entry::getKey));

        for (Map.Entry<LocalDate, Integer> entry : sortedEntries) {
            keyList.add(entry.getKey().format(formatter));
            valueList.add(entry.getValue());
        }
        // 遍历Map，将key和value分别添加到对应的List中
//        for (Map.Entry<LocalDate, Integer> entry : dailyAlertCount.entrySet()) {
//            keyList.add(entry.getKey().format(formatter));
//            valueList.add(entry.getValue());
//        }

        // 遍历获取告警设备榜数据
        List<String> warningName = new ArrayList<>();
        List<Integer> warningTotal = new ArrayList<>();
        List<Integer> warningHandle = new ArrayList<>();
        for (WarningEquipmentVO equipmentVO : warningEquipmentVOS) {
            warningName.add(equipmentVO.getEquipmentName());
            warningTotal.add(equipmentVO.getTotal());
            warningHandle.add(equipmentVO.getHandleCount());
        }

        // 存储告警总数
        homeDO.setActionCount(configActionDOList.size());
        homeDO.setConfigCount(configCount);
        homeDO.setConfigEnableCount(configEnableCount);
        homeDO.setConfigDisableCount(configDisableCount);
        homeDO.setRecordCount(recordDOS.size());
        homeDO.setTimeList(keyList);
        homeDO.setWarningCountList(valueList);
        homeDO.setWarningNameList(warningName);
        homeDO.setWarningTotalList(warningTotal);
        homeDO.setWarningHandleList(warningHandle);
        homeDO.setActionType(actionType);
//        homeDO.setRuleNameList(ruleNameList);
        homeDO.setUseCountMap(map);

        return CommonResult.success(homeDO);
    }

    /**
     * 获取设备关联告警数
     * @return
     */
    @GetMapping("/getEquipmentRelateWarning")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取设备关联告警数")
    public CommonResult<List<EquipmentRelateWarningDTO>> getEquipmentRelateWarning() {
        List<EquipmentRelateWarningVO> equipmentIds = warningService.getEquipmentRelateWarning();
        List<EquipmentRelateWarningDTO> equipmentRelateWarningDTOS = new ArrayList<>();
        for (EquipmentRelateWarningVO equipmentId : equipmentIds) {
            EquipmentRelateWarningDTO relateWarningDTO = new EquipmentRelateWarningDTO();
            relateWarningDTO.setWarningCount(equipmentId.getWarningCount());
            relateWarningDTO.setEquipmentId(equipmentId.getEquipmentId());
            equipmentRelateWarningDTOS.add(relateWarningDTO);
        }
        List<EquipmentRelateWarningDTO> equipmentDetail = equipmentApi.getEquipmentDetail(equipmentRelateWarningDTOS);
        return CommonResult.success(equipmentDetail);
    }
}
