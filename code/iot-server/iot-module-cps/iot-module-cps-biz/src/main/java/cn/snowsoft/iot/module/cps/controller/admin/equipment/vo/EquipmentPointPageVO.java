package cn.snowsoft.iot.module.cps.controller.admin.equipment.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@TableName(value = "cps_equipment_point", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentPointPageVO extends PageParam {
    private static final long serialVersionUID = 1L;

    /**
     * 设备Id
     */
    private Long equipmentId;

    /**
     * 点位数据
     */
    private Integer pointDataNumber;

    /**
     * 采集时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectionTime;

    /**
     * 点位编码
     */
    private String pointCode;

    /**
     * 点位名称
     */
    private String pointName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 转换公式
     */
    private String formula;

    /**
     * 读写
     */
    private String readAndWrite;

    /**
     * 读指令
     */
    private Integer readInstruction;

    /**
     * 写指令
     */
    private Integer writeInstruction;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 寄存器地址
     */
    private Integer registerId;

    /**
     * 寄存器数量
     */
    private Integer quantity;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 点位类型
     */
    private String pointType;

    /**
     * 输入参数
     */
    private String inputParameter;

    /**
     * 输出参数
     */
    private String outputParameter;

}
