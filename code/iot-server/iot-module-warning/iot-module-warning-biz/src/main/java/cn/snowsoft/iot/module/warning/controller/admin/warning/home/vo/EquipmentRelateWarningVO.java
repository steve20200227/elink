package cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class EquipmentRelateWarningVO {

    /**
     * 设备id
     */
    private Long equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 告警数
     */
    private Integer warningCount;

}
