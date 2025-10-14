package cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningPageVO extends PageParam {

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 告警名称
     */
    private String warningName;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 设备id
     */
//    private Long equipmentId;

    /**
     * 点位id
     */
    private Long pointId;

    /**
     * 规则条件
     */
    private String ruleCondition;

    /**
     * 规则表达式
     */
    private String ruleExpression;

    /**
     * 规则SQL
     */
    private String ruleSql;

    /**
     * 规则阈值
     */
    private String ruleThreshold;
    /**
     * 适用设备编码集合
     */
    private String equipmentCode;
}
