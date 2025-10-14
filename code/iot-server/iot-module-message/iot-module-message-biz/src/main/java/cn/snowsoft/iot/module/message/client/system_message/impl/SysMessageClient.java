package cn.snowsoft.iot.module.message.client.system_message.impl;

import cn.snowsoft.iot.module.message.client.system_message.SystemMessageClient;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.system.api.notify.NotifyMessageSendApi;
import cn.snowsoft.iot.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class SysMessageClient implements SystemMessageClient {

    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;
    @Override
    public void sendSms(SendTaskDto sendTaskDto) {
        NotifySendSingleToUserReqDTO notifySendSingleToUserReqDTO = new NotifySendSingleToUserReqDTO();

        notifyMessageSendApi.sendSingleMessageToMember(new NotifySendSingleToUserReqDTO());
    }
}
