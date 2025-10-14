package cn.snowsoft.iot.module.cps.dal.dataobject.monitoring;


import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数采监控
 */
@TableName(value = "cps_monitoring")
@Data
@EqualsAndHashCode(callSuper = true)
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
@ExcelIgnoreUnannotated
@HeadStyle(fillForegroundColor = 40)
public class Monitoring extends TenantEntity {
  /**
   * 乐观锁
   */
  @TableField(value = "revision")
  @ExcelProperty(value = "序号", index = 0)
  @ColumnWidth(12)
  private Integer revision;
  /**
   * 备注
   */
  @TableField(value = "remark")
  private String remark;
  /**
   * 终端编码
   */
  @ExcelProperty(value = "终端编码", index = 1)
  @HeadFontStyle(color = 69)
  @TableField(value = "terminal_coding")
  private String terminalCoding;
  /**
   * 终端名称
   */
  @ExcelProperty(value = "终端名称", index = 2)
  @HeadFontStyle(color = 69)
  @TableField(value = "terminal_name")
  private String terminalName;
  /**
   * 协议名称id
   */
  @TableField(value = "agreement_nameid")
  private String agreementNameid;
  /**
   * 协议名称
   */
  @ExcelProperty(value = "协议名称", index = 3)
  @HeadFontStyle(color = 69)
  @TableField(value = "agreement_name")
  private String agreementName;
  /**
   * 是否启用
   */
  @ExcelProperty(value = "是否启用", index = 4)
  @HeadFontStyle(color = 69)
  @TableField(value = "is_enable")
  private String isEnable;
  /**
   * 当前状态
   */
  @ExcelProperty(value = "当前状态", index = 5)
  @HeadFontStyle(color = 69)
  @TableField(value = "is_status")
  private String isStatus;
  /**
   * 协议配置参数json
   */
  @ExcelProperty(value = "协议配置参数", index = 6)
  @HeadFontStyle(color = 69)
  @TableField(value = "protocol_params")
  private String protocolParams;
  /**
   * ip地址
   */
  @ExcelProperty(value = "ip地址", index = 7)
  @HeadFontStyle(color = 69)
  @TableField(value = "ip_location")
  private String ipLocation;
  /**
   * clsid
   */
  @ExcelProperty(value = "clsid", index = 8)
  @HeadFontStyle(color = 69)
  @TableField(value = "clsid")
  private String clsid;
  /**
   * 采集周期
   */
  @TableField(value = "heartbeat_topic")
  private Integer heartbeatTopic;

  /**
   * 设备编号
   */
  @TableField(exist = false)
  private String deviceCode;
  /**
   * 测点编号
   */
  @TableField(exist = false)
  private String gaugingType;

  @TableField(exist = false)
  private Long enableDeviceCount;
  @TableField(exist = false)
  private Long disableDeviceCount;
}
