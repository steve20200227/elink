package cn.snowsoft.iot.module.job.framework.rpc.config;

import cn.snowsoft.iot.module.job.api.XxljobAdminApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {XxljobAdminApi.class})
public class RpcConfiguration {

}
