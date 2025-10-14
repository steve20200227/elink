package cn.snowsoft.iot.module.cps.controller.admin.product.vo;

import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import lombok.Data;

@Data
public class ProductVO extends ProductDO {

    /**
     * 分类名称
     */
    private String sortName;
    /**
     * 上报id
     */
    private Long reportId;
    /**
     * 下发id
     */
    private Long deliverAnId;
    /**
     * 下发id
     */
    private ProductDO parentProductDO;
}
