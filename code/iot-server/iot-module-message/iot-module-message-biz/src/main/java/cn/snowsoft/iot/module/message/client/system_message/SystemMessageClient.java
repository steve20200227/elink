package cn.snowsoft.iot.module.message.client.system_message;

import cn.snowsoft.iot.module.message.model.app.sms.SmsApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;

public interface SystemMessageClient {

    /**
     * 发送站内信
     *
     * @param sendTaskDto 发送任务
     */
    void sendSms(SendTaskDto sendTaskDto);
}
