package cn.snowsoft.iot.module.cps.dal.mysql.equipment;

import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPointPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EquipmentPointMapper extends BaseMapper<EquipmentPointDO> {
    IPage<EquipmentPointDO> selectPointPage(IPage<EquipmentPointDO> page,@Param("entity") EquipmentPointPageVO point);
}
