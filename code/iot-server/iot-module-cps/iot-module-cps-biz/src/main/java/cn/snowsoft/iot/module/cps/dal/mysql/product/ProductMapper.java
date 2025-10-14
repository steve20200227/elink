package cn.snowsoft.iot.module.cps.dal.mysql.product;

import cn.snowsoft.iot.module.cps.controller.admin.home.vo.ProductAssociationEquipmentVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.ProductStatusVO;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPage;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<ProductDO> {
    IPage<ProductPage> selectProductPage(IPage<ProductPage> page, @Param("entity") ProductPageVO product);

    List<ProductAssociationEquipmentVO> getEquipmentCount();

    List<ProductStatusVO> getIsEnableCount();

    List<ProductPage> isAssociationEquipment(@Param("ids") List<Long> ids);
}
