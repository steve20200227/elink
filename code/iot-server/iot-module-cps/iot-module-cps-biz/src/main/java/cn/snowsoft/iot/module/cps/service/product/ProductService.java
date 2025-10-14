package cn.snowsoft.iot.module.cps.service.product;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.ProductAssociationEquipmentVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.ProductStatusVO;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPage;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProductService extends IService<ProductDO> {

    CommonResult<Boolean> removeProduct(List<Long> ids);

    IPage<ProductPage> selectProductPage(ProductPageVO pageVO);

    List<ProductAssociationEquipmentVO> getEquipmentCount();

    List<ProductStatusVO> getIsEnableCount();

    List<ProductDO> getByProductSortId(Long id);

    ProductDO getByCode(String productCode);

    CommonResult addAuthentication(ProductPage productDo);
}
