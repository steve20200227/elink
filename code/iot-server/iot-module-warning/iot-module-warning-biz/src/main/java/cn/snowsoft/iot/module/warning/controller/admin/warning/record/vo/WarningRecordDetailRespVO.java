package cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo;

import lombok.Data;

import java.util.List;

@Data
public class WarningRecordDetailRespVO {
    /**
     * 主键
     */

    private Long id;

    /**
     * 动作名称
     */
    private String actionName;

    /**
     * 动作类型
     */
    private String outputWay;

    private String actionType;




    private List<Object> dataList;

    private Integer failCount;

    private Integer successCount;


}
