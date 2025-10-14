package cn.snowsoft.iot.module.cps.dal.dataobject.product;

import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value = "cps_product", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDO extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 备注
     */
    private String remark;

    /**
     * 协议类型
     */
    private String agreementType;

    /**
     * 图片路径
     */
    private String image;

    /**
     * 产品分类id
     */
    @TableField("product_sort_id")
    private Long productSortId;

    /**
     * 设备类型：直连设备；子设备；网关设备；
     */
    private String equipmentType;

    /**
     * 上报布地址
     */
    private String pushAddress;

    /**
     * 发布地址
     */
    private String issuedAddress;

    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 解析协议
     */
    private String parsingProtocol;

    /**
     * 接入地址
     */
    private String serviceIp;

    /**
     * 关联父产品（数据库存储产品编码）
     */
    private String parentProduct;

    /**
     * 属性上报
     */
    private String attributePush;

    /**
     * 事件上报
     */
    private String eventPush;

    /**
     * 功能下推
     */
    private String featureIssued;

}
