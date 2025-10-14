package cn.snowsoft.iot.module.warning.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: chen_gang
 * @date: 2024/8/12 15:46
 * @description: 配置事件异步处理线程池
 */
@Configuration
public class EventExecutorConfig {

    @Bean("eventAsyncExecutor")
    public ExecutorService eventAsyncExecutor() {
        return Executors.newCachedThreadPool();
    }
}
