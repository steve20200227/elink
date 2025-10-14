package cn.snowsoft.iot.module.warning.dal.mysql.warning.config;

import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.EquipmentVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.EquipmentRelateWarningVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.WarningConfigStatusVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningPageVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.WarningDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WarningMapper extends BaseMapper<WarningDO> {
    IPage<WarningDO> selectProductPage(IPage<WarningDO> page, @Param("entity") WarningPageVO warningPageVO);

    @Select("select * from warning_config where deleted = 0 and rule_id = #{id}")
    WarningDO getByRuleId(Long id);

    List<WarningDO> getWarningCountByEquipmentId(@Param("equipmentCodeList") List<String> equipmentCodeList);

    @Select("select count(*) from warning_config where deleted = 0")
    Integer getConfifCount();

    List<EquipmentRelateWarningVO> getEquipmentRelateWarning();

    @Select("select * from warning_config where deleted = 0 and equipment_id = #{id}")
    List<WarningDO> selectByEquipmentId(Long id);

    List<WarningConfigStatusVO> getStatusCount();

    List<WarningDO> getWarningByEquipmentCode(@Param("equipmentCode") String equipmentCode);

    List<EquipmentVO> getEquipmentNameByCodes(@Param("equipmentCodes") List<String> list);
}
