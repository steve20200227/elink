package cn.snowsoft.iot.module.cps.dal.dataobject.equipment;

import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import lombok.*;

@TableName(value = "cps_equipment", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDO extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编码
     */
    private String equipmentCode;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 采集设备Id
     */
    @JsonSerialize(nullsUsing = NullSerializer.class)
    private Long collectionId;

    /**
     * 采集设备编码
     */
    @JsonSerialize(nullsUsing = NullSerializer.class)
    private String collectionCode;

    /**
     * 舱室编码
     */
    private String cabinCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 网络位置
     */
    private String netLocation;

    /**
     * 图片地址
     */
    private String image;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 备注
     */
    private String remark;


    /**
     * websocket服务端地址，供数据监控界面使用
     */
    @TableField(exist = false)
    private String webSocket;

    /**
     * 协议类型
     */
    private String agreementType;

    /**
     * 设备所属
     */
    private String equipmentAscription;
    /**
     * 二维码
     */
    private String qrCode;

    /**
     * 设备类型：直连设备；子设备；网关设备；
     */
    private String equipmentType;

    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 关联父设备（数据库存储设备编码）
     */
    private String parentEquipment;

    /**
     * 位置
     */
    private String location;

    /**
     * 解析协议
     */
    private String parsingProtocol;

    /**
     * 接入地址
     */
    private String serviceIp;

    /**
     * 属性上报
     */
    private String attributePush;

    /**
     * 事件上报
     */
    private String eventPush;

    /**
     * 功能下推
     */
    private String featureIssued;

    private String isHistory;


}
