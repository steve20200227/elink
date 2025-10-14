package cn.snowsoft.iot.module.cps.dal.dataobject.product;

import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value = "cps_product_point", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPointDO extends TenantEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 产品id
     */
    private Long productId;

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
    /*
    * 协议类型
    * */
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
