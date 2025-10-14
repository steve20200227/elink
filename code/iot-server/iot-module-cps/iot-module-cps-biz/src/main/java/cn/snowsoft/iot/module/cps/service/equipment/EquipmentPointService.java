package cn.snowsoft.iot.module.cps.service.equipment;

import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPointPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentQueryVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface EquipmentPointService extends IService<EquipmentPointDO> {

    IPage<EquipmentPointDO> selectPointPage(EquipmentPointPageVO point);

    Map getDeviceAttributeAndEvent(String equipmentCode);

    Boolean deleteLogic(List<Long> ids);

    EquipmentPointDO submit(EquipmentPointDO point);

    Boolean coveragePoint(Long equipmentId, Long productId);

    void removePoint(Long id);

    List<EquipmentPointDO> getByEquipmentId(Long equipmentId);

    List<EquipmentPointDO> getByEquipmentIdsAndCode(EquipmentQueryVO equipmentQueryVO);

    List<EquipmentPointDO> getList(EquipmentPageVO point);
}
