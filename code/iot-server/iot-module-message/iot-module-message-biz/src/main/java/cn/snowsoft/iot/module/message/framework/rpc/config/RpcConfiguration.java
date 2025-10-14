package cn.snowsoft.iot.module.message.framework.rpc.config;

import cn.snowsoft.iot.module.infra.api.file.FileApi;
import cn.snowsoft.iot.module.system.api.notify.NotifyMessageSendApi;
import cn.snowsoft.iot.module.system.api.user.AdminUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class, NotifyMessageSendApi.class, AdminUserApi.class})
public class RpcConfiguration {
}
