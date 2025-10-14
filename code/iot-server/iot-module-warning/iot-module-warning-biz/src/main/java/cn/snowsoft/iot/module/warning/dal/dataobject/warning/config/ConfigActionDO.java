package cn.snowsoft.iot.module.warning.dal.dataobject.warning.config;

import cn.snowsoft.iot.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 告警实体类
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warning_config_action")
public class ConfigActionDO extends BaseDO {

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 动作id
     */
    private Long actionId;

    /**
     * 动作名称
     */
    private String actionName;

    /**
     * 动作类型
     */
    private String actionType;

    /**
     * 告警配置ID
     */
    private Long configId;


    private String relatedParameter;

    private String outputWay;

    /**
     * 源ids
     */
    private String sourceIds;

    /**
     * 产品名称
     */
    private String productName;
}