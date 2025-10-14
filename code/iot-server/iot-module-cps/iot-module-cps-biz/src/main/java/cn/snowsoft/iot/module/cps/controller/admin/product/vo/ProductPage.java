package cn.snowsoft.iot.module.cps.controller.admin.product.vo;

import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import lombok.Data;

@Data
public class ProductPage extends ProductDO {

    /**
     * 关联设备数
     */
    private Integer equipmentCount;

    /**
     * 认证器状态：0：新增，1：更新
     */
    private Integer authenticationStatus;

    private String oldUserAccount;

    private String oldUserPassword;
}
