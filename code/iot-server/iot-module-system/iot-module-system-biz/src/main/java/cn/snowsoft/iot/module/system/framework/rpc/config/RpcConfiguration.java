package cn.snowsoft.iot.module.system.framework.rpc.config;

import cn.snowsoft.iot.module.infra.api.file.FileApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class})
public class RpcConfiguration {
}
