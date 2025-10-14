package cn.snowsoft.iot.module.system.api.notify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Schema(description = "RPC 服务 - 站内信发送给 Admin 或者 Member 用户 Request DTO")
@Data
public class NotifySendMessageUserReqDTO implements Serializable {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long userId;

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "USER_SEND")
    private Long templateId;


    @Schema(description = "模板内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "USER_SEND")
    private String templateContent;



    @Schema(description = "模板参数")
    private Map<String, Object> templateParams;

}
