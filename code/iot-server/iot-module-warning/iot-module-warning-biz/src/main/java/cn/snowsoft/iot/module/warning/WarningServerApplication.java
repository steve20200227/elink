package cn.snowsoft.iot.module.warning;

import cn.snowsoft.iot.module.job.api.XxljobAdminApi;
import cn.snowsoft.iot.module.message.api.send.SendApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 项目的启动类
 *
 * @author chen_gang
 */
@SpringBootApplication
@EnableAsync
@EnableFeignClients(clients = {SendApi.class, XxljobAdminApi.class})
public class WarningServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarningServerApplication.class, args);
    }

}
