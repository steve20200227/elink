package cn.snowsoft.iot.module.warning.framework.rpc.config;

import cn.snowsoft.iot.module.cps.api.equipment.AdminEquipmentApi;
import cn.snowsoft.iot.module.cps.api.equipment.AdminPointApi;
import cn.snowsoft.iot.module.infra.api.file.FileApi;
import cn.snowsoft.iot.module.job.api.XxljobAdminApi;
import cn.snowsoft.iot.module.message.api.messageRecord.MessageRecordApi;
import cn.snowsoft.iot.module.message.api.send.SendApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class, AdminPointApi.class, AdminEquipmentApi.class,
        SendApi.class, MessageRecordApi.class,XxljobAdminApi.class})
public class RpcConfiguration {
}
