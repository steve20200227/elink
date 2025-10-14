package cn.snowsoft.iot.module.cps.dal.mysql.product;

import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPointPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductPointDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductPointMapper extends BaseMapper<ProductPointDO> {
    IPage<ProductPointDO> selectPointPage(IPage<ProductPointDO> page,@Param("entity") ProductPointPageVO point);
}
