package cn.snowsoft.iot.module.cps.controller.admin.product.vo;


import cn.snowsoft.iot.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value = "cps_product_point", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPointPageVO extends PageParam {
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
