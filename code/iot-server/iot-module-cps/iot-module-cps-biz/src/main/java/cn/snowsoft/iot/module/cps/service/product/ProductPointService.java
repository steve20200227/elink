package cn.snowsoft.iot.module.cps.service.product;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPointPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductPointDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ProductPointService extends IService<ProductPointDO> {
    CommonResult submit(ProductPointDO point);

    Boolean deleteLogic(List<Long> ids);

    IPage<ProductPointDO> selectPointPage(ProductPointPageVO point);

    void removePoint(Long id);

    List<ProductPointDO> getByProductId(Long productId);
}
