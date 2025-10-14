package cn.snowsoft.iot.module.cps.api.equipment;

import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentDTO;
import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentRelateWarningDTO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import cn.snowsoft.iot.module.cps.service.equipment.EquipmentService;
import cn.snowsoft.iot.framework.common.util.object.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class AdminEquipmentImpl implements AdminEquipmentApi{

    @Resource
    private EquipmentService equipmentService;

    @Override
    public EquipmentDTO detail(Long id) {
        EquipmentDO byId = equipmentService.getById(id);
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        org.springframework.beans.BeanUtils.copyProperties(byId, equipmentDTO);
        return equipmentDTO;
    }

    @Override
    public List<EquipmentRelateWarningDTO> getEquipmentDetail(List<EquipmentRelateWarningDTO> equipmentRelateWarningDTOS) {
        List<Long> equipmentIds = new ArrayList<>();
        for (EquipmentRelateWarningDTO equipment : equipmentRelateWarningDTOS) {
            equipmentIds.add(equipment.getEquipmentId());
        }
        List<EquipmentRelateWarningDTO> equipmentNames = equipmentService.getEquipmentName(equipmentIds);
        for (EquipmentRelateWarningDTO relateWarning : equipmentRelateWarningDTOS) {
            for (EquipmentRelateWarningDTO equipmentName : equipmentNames) {
                if (relateWarning.getEquipmentId().equals(equipmentName.getEquipmentId())) {
                    relateWarning.setEquipmentName(equipmentName.getEquipmentName());
                }
            }
        }
        System.out.println("equipmentRelateWarningDTOS = " + equipmentRelateWarningDTOS);
        return equipmentRelateWarningDTOS;
    }

    @Override
    public List<EquipmentDTO> getEquipmentCode(List<String> equipmentCode) {
        List<EquipmentDO> equipmentDOS = equipmentService.lambdaQuery().in(EquipmentDO::getEquipmentCode, equipmentCode).list();
        List<EquipmentDTO> sourceList = new ArrayList<>();
        equipmentDOS.stream().forEach(e->{
            EquipmentDTO equipmentDTO = new EquipmentDTO();
            org.springframework.beans.BeanUtils.copyProperties(e, equipmentDTO);
            sourceList.add(equipmentDTO);
        });
        return sourceList;
    }
}
