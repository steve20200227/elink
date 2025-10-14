package cn.snowsoft.iot.module.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 项目的启动类
 *
 * @author 芋道源码
 */
@SpringBootApplication
@EnableCaching //开启缓存功能
@EnableAsync // 开启异步功能
@MapperScan("cn.snowsoft.iot.module.message.mapper")
@EnableFeignClients
public class MessageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageServerApplication.class, args);
    }

}
