package cn.snowsoft.iot.module.message.api.send;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.message.api.send.dto.SendMessageRequestDto;
import cn.snowsoft.iot.module.message.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 消息")
public interface SendApi {
    String PREFIX = ApiConstants.PREFIX + "/open";

    @PostMapping(PREFIX + "/sendMessageTest")
    @Operation(summary = "发送消息")
    @Parameter(name = "", description = "", example = "", required = true)
    CommonResult<String> sendMessage(@Valid @RequestBody SendMessageRequestDto sendMessageRequestDto);

}
