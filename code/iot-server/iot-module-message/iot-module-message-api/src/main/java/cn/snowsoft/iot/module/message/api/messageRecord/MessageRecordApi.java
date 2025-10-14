package cn.snowsoft.iot.module.message.api.messageRecord;

import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRequestDto;
import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;
import cn.snowsoft.iot.module.message.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = ApiConstants.NAME) //
@Tag(name = "RPC 服务 - 消息")
public interface MessageRecordApi {
    String PREFIX = ApiConstants.PREFIX + "/record";

    @PostMapping(PREFIX + "/getMessageRecordDetail")
    @Operation(summary = "查询消息记录详情")
    @Parameter(name = "", description = "", example = "", required = true)
    List<MessageRecordRespDto> getMessageRecordDetail(@RequestBody MessageRecordRequestDto messageRecordRequestDto);

}
