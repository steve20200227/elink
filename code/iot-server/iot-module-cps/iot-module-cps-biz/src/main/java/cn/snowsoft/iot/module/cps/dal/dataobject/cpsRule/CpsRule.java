package cn.snowsoft.iot.module.cps.dal.dataobject.cpsRule;

import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 规则实体类
 *
 * @author Chill
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cps_rule")
public class CpsRule extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 规则编码同步mqtt  id
     */
    private String ruleCode;

    /**
     * 是否启用 1是2否
     */
    private String isEnable;
    /**
     * 备注
     */
    private String remark;
    /**
     * sql
     */
    private String ruleSql;
    /**
     * 1.产品2.设备3.规则
     */
    private String ruleType;
    /**
     * 动作actionType:动作类型动作主题：actionTheme
     * Payload：Payload
     */
    private String action;
    /**
     * inputTheme:输入主题inputType输入类型
     */
    private String ruleInput;
    /**
     * 关联的设备或者产品的id
     */
    private Long relevanceId;
    /**
     * 1上报2下发
     */
    private String type;


}