package cn.snowsoft.iot.module.message.api.messageRecord.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageRecordRequestDto implements Serializable {

    private String warningRecordId;

    private Long warningActionId;

}
