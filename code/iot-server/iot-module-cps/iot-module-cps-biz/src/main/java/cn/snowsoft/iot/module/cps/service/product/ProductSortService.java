package cn.snowsoft.iot.module.cps.service.product;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductSortVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductSortDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProductSortService extends IService<ProductSortDO> {

    List<ProductSortVO> getAllProductSorts();

    ProductSortVO saveData(ProductSortVO productSortVO);

    CommonResult<Boolean> verifyRemove(Long id);
}
