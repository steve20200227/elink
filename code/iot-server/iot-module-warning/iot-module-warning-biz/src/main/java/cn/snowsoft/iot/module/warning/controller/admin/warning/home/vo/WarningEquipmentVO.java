package cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class WarningEquipmentVO {
    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 设备 id
     */
    private Long equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 告警状态
     */
    private Integer status;

    /**
     * 告警总数
     */
    private Integer total;

    /**
     * 处理数
     */
    private Integer handleCount;

}
