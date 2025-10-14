package cn.snowsoft.iot.module.cps.service.equipment;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentRelateWarningDTO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPage;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.EquipmentAgreementTypeVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.EquipmentStatusVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.dto.EquipmentBatchAddDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface EquipmentService extends IService<EquipmentDO> {
    Boolean enableEquipment(List<Long> ids);

    Boolean disenableEquipment(List<Long> ids);

    Boolean removeEquipment(EquipmentPage equipment);

    Object restart();

    CommonResult submit(EquipmentDO equipment);

    IPage<EquipmentPage> selectEquipmentPage(EquipmentPageVO equipment);

    List<EquipmentDO> selectEquipmentList(EquipmentDO equipment);

    Boolean relevancyEquipment(List<Long> ids, Long passageId);

    Boolean deleteEquipmentByPassageId(Long id);

    EquipmentDO detailMonitor(Long id);

    List<EquipmentPage> selectEquipmentPageByProductCode(EquipmentPageVO equipment);

    List<EquipmentAgreementTypeVO> getAgreementTypeCount();

    List<EquipmentRelateWarningDTO> getEquipmentName(List<Long> equipmentIds);

    List<EquipmentStatusVO> getIsEnableCount();

    CommonResult cancelPassage(String id);

    List<EquipmentDO> getEnableList();

    List<EquipmentDO> getByIds(String ids);

    CommonResult getChildEquipment(String equipmentCode);

    IPage<EquipmentDO> selectRuleList(EquipmentPageVO equipment);

    CommonResult addAuthentication(EquipmentPage equipment);

    String generateCode();

    CommonResult batchAdd(EquipmentBatchAddDTO equipment);
}
