package cn.snowsoft.iot.module.message.api;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.message.api.send.SendApi;
import cn.snowsoft.iot.module.message.api.send.dto.SendMessageRequestDto;
import cn.snowsoft.iot.module.message.model.dto.request.SendRequestDto;
import cn.snowsoft.iot.module.message.service.SendService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SendApiImpl implements SendApi {
    @Resource
    private SendService sendService;
    @Override
    public CommonResult<String> sendMessage(SendMessageRequestDto sendMessageRequestDto) {
        SendRequestDto sendRequestDto = new SendRequestDto();
        BeanUtils.copyProperties(sendMessageRequestDto,sendRequestDto);

        String send = sendService.send(sendRequestDto);
        return CommonResult.success(send);
    }
}
