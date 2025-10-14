package cn.snowsoft.iot.module.message.api;

import cn.snowsoft.iot.module.message.api.messageRecord.MessageRecordApi;
import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRequestDto;
import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;

import cn.snowsoft.iot.module.message.controller.admin.vo.MessageRecordVO;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class MessageRecordApiImpl implements MessageRecordApi {
    @Resource
    private MessageRecordService messageRecordService;
    @Override
    public List<MessageRecordRespDto> getMessageRecordDetail(MessageRecordRequestDto messageRecordRequestDto) {
        MessageRecordVO messageRecordVO = new MessageRecordVO();
        BeanUtils.copyProperties(messageRecordRequestDto,messageRecordVO);
        List<MessageRecordRespDto> detail = messageRecordService.getDetail(messageRecordVO);

        return detail;
    }
}
