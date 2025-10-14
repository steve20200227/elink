package cn.snowsoft.iot.module.cps.controller.admin.product.vo;


import cn.snowsoft.iot.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value = "cps_product", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageVO extends PageParam {

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
     * 产品分类id
     */
    @TableField("product_sort_id")
    private Long productSortId;
}
