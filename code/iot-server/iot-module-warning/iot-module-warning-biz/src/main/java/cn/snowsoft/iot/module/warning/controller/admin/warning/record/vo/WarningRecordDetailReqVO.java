package cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;
import lombok.Data;

import java.util.List;

@Data
public class WarningRecordDetailReqVO extends PageParam {
    //告警动作id
    private Long configId;


    //告警UUID
    private String warningTag;

    //动作类型
    private String actionType;

    //北向输出动作id
    private Long actionId;

    private String outputStatus;

    private String outputWay;

    private List<String> warningTagList;
}
