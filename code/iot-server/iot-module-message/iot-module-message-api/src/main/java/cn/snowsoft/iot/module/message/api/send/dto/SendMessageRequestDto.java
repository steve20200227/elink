package cn.snowsoft.iot.module.message.api.send.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequestDto {

    /**
     * 消息模板 Id
     */
    @NotNull(message = "消息模板 ID 不能为 NULL")
    private Long templateId;

    /**
     * 发送用户列表
     */
    @NotEmpty(message = "发送用户列表不能为空")
    private List<String> users;

    /**
     * 不同消息的不同参数
     */
    @NotNull(message = "消息参数不能为 NULL")
    private Map<String, Object> paramMap;

    //告警记录id
    private String warningRecordId;

    //执行动作id
    private Long warningActionId;

    /**
     * 重试次数
     * 默认为 0
     */
    private Integer retry = 0;
}
