package cn.snowsoft.iot.module.cps.framework.rpc.config;

import cn.snowsoft.iot.module.infra.api.file.FileApi;
import cn.snowsoft.iot.module.warning.enums.api.config.AdminConfigApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class, AdminConfigApi.class})
public class RpcConfiguration {
}
