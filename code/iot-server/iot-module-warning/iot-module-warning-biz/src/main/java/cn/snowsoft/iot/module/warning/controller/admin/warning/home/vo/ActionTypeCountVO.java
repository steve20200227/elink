package cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo;

import lombok.Data;

@Data
public class ActionTypeCountVO {

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 输出方式
     */
    private String outputWay;

    /**
     * 动作数量
     */
    private Integer actionCount;

}
