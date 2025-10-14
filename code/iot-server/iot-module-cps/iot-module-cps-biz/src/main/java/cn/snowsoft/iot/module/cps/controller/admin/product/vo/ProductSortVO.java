package cn.snowsoft.iot.module.cps.controller.admin.product.vo;

import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductSortDO;
import lombok.Data;

import java.util.List;

@Data
public class ProductSortVO extends ProductSortDO {

    /**
     * 子分类
     */
    private List<ProductSortVO> children;
}
