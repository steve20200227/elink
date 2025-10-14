package cn.snowsoft.iot.module.cps.dal.mysql.equipment;

import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentRelateWarningDTO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPage;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.EquipmentAgreementTypeVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.EquipmentStatusVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EquipmentMapper extends BaseMapper<EquipmentDO> {
    IPage<EquipmentPage> selectEquipmentPage(IPage<EquipmentPage> page, @Param("entity") EquipmentPageVO equipment);

    Boolean relevancyEquipment(@Param("ids") List<Long> ids, @Param("passageId") Long passageId);

    @Update("update cps_equipment set equipment_ascription = 'none' where equipment_ascription = #{id}")
    List<EquipmentDO> deleteEquipmentByPassageId(String id);

    List<EquipmentPage> selectEquipmentPageByProductCode(@Param("entity") EquipmentPageVO equipment);

    List<EquipmentAgreementTypeVO> getAgreementTypeCount();

    List<EquipmentRelateWarningDTO> getEquipmentName(@Param("ids")List<Long> equipmentIds);

    List<EquipmentStatusVO> getIsEnableCount();

    @Update("update cps_equipment set equipment_ascription = null where id = #{id}")
    boolean cancelPassage(String id);

    List<EquipmentDO> getChildEquipment(@Param("equipmentCode") String equipmentCode);

    IPage<EquipmentDO> selectRuleList(IPage<EquipmentPage> page, @Param("entity") EquipmentPageVO equipment);
}
