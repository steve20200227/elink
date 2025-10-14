package cn.snowsoft.iot.module.warning.dal.mysql.warning.north;

import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.ActionTypeCountVO;
import cn.snowsoft.iot.module.warning.controller.admin.north.vo.NorthPageVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.NorthDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NorthMapper extends BaseMapper<NorthDO> {
    IPage<NorthDO> selectActionPage(IPage<NorthDO> page, @Param("entity") NorthPageVO actionPage);

    List<ActionTypeCountVO> getActionTypeCount();
}
