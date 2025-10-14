package cn.snowsoft.iot.module.cps.dal.dataobject.equipment;

import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@TableName(value = "cps_equipment_point", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EquipmentPointDO extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 是否是覆盖带出的关联的产品点位：0：是；1：否
     */
    private Integer isCoverage;

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
     * 来源
     */
    private String source;

    /**
     * 协议类型
     */
    private String agreementType;

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

    /**
     * 属性字典
     */
    private String pointDict;

}
