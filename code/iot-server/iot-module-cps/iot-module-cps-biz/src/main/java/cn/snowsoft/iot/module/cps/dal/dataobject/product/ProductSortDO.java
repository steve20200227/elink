package cn.snowsoft.iot.module.cps.dal.dataobject.product;

import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value = "cps_product_sort", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSortDO extends TenantEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 备注
     */
    private String remark;

    /**
     * 分类名称
     */
    private String sortName;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 祖籍节点id列表
     */
    private String progenitorId;

}
