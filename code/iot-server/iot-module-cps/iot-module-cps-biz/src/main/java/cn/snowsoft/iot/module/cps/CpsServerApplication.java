package cn.snowsoft.iot.module.cps;

import cn.snowsoft.iot.module.job.api.XxljobAdminApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 项目的启动类
 * @author chengang
 */
@SpringBootApplication
@EnableFeignClients(clients = {XxljobAdminApi.class})
public class CpsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpsServerApplication.class, args);
    }

}
