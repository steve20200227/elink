package cn.snowsoft.iot.module.cps.utils;

import cn.snowsoft.iot.module.cps.service.equipment.EquipmentService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableScheduling
public class SyncEquipmentUtil {

    @Resource
    private EquipmentService equipmentService;

//    @Scheduled(fixedRate = 5 * 60 * 1000)
//    public void runEvery5Seconds() {
//        List<EquipmentDO> enableList = equipmentService.getEnableList();
//        Map<String, String> deviceCodeTopicMap = enableList.stream()
//                .filter(e -> e.getIsEnable() == 1)
//                .collect(Collectors.toMap(EquipmentDO::getEquipmentCode, EquipmentDO::getAttributePush));
//        InitCpsServer.deviceCodeTopic.putAll(deviceCodeTopicMap);
//    }

}
