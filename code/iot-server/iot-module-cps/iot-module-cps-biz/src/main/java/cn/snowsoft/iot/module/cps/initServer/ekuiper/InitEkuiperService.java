package cn.snowsoft.iot.module.cps.initServer.ekuiper;

import cn.snowsoft.iot.module.cps.initServer.ekuiper.handler.DeviceStatusSourceHandler;
import cn.snowsoft.iot.module.cps.initServer.ekuiper.handler.SourceHandler;
import cn.snowsoft.iot.module.cps.initServer.ekuiper.handler.BaseHandler;
import cn.snowsoft.iot.module.cps.initServer.ekuiper.handler.ConnectorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 在服务启动时进行 ekuiper计算引擎的连接器和源配置的自动化新增
 */
@Component
@DependsOn({"ekuiperProperties"})
public class InitEkuiperService {

    @Autowired
    private EkuiperProperties ekuiperProperties;

    /**
     * 在服务启动时进行 ekuiper计算引擎的连接器和源配置的自动化新增
     */
    @PostConstruct
    public void process() {
        //创建ekuiper连接器的处理器
        BaseHandler connectorHandler = new ConnectorHandler();
        //创建ekuiper源配置的处理器
        BaseHandler sourceHandler = new SourceHandler();
        //创建ekuiper设备状态源配置的处理器
        BaseHandler deviceStatusSourceHandler = new DeviceStatusSourceHandler();
        //设置处理器的顺序，构建为一个处理链
        connectorHandler.setNextHandler(sourceHandler);
        sourceHandler.setNextHandler(deviceStatusSourceHandler);
        //开启处理链的执行
        connectorHandler.handleRequest(ekuiperProperties);
    }
}
