package cn.snowsoft.iot.module.warning.enums.api.config.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigCountVO implements Serializable {

//    /**
//     * 设备id
//     */
//    private Long equipmentId;
    /**
     * 设备code
     */
    private String equipmentCode;

    /**
     * 告警数量
     */
    private Integer warningCount;

}
