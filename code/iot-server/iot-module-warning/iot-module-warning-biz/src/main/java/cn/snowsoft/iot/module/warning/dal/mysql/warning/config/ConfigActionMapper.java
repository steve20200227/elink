package cn.snowsoft.iot.module.warning.dal.mysql.warning.config;

import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ConfigActionMapper extends BaseMapper<ConfigActionDO> {

    @Update("update warning_config_action set deleted = 1 where config_id = #{id}")
    void deleteByWarningId(Long id);

    @Select("select * from warning_config_action where deleted = 0 and config_id = #{id}")
    List<ConfigActionDO> selectByConfigId(Long id);
}
