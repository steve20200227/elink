package cn.snowsoft.iot.module.warning.dal.dataobject.warning.config;

import lombok.Data;

/**
 * 场景配置条件信息类
 *
 */
@Data
public class ConfigRule {

    /**
     * 条件关联类型  and or where
     */
    private String whereType;

    /**
     * 点位编码
     */
    private String pointCode;

    /**
     * 参与值类型, 当前值,上一次值等
     */
    private String valueType;

    /**
     * 判断符类型, 大于,小于等
     */
    private String conditionType;

    /**
     * 参数1
     */
    private double param1;

    /**
     * 参数2
     */
    private double param2;
}