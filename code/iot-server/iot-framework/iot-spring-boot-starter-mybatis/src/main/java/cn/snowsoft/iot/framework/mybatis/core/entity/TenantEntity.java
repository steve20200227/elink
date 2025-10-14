package cn.snowsoft.iot.framework.mybatis.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拓展多租户的基类
 *
 * @author raft
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantEntity extends BaseEntity {

    /**
     * 多租户编号
     */
    private String tenantId;

}
