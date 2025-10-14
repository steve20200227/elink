package cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo;

import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningActionRecord;
import lombok.Data;

@Data
public class WarningActionRecordRespVO extends WarningActionRecord {
    private String actionName;
    private String outputWay;
}
