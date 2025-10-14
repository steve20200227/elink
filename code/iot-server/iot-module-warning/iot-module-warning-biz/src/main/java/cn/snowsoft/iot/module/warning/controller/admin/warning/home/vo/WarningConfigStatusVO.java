package cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo;

import lombok.Data;

@Data
public class WarningConfigStatusVO {

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 数量
     */
    private Integer count;

}
