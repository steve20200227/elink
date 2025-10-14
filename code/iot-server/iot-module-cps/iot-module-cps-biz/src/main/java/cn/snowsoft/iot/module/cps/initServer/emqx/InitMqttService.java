package cn.snowsoft.iot.module.cps.initServer.emqx;

import cn.snowsoft.iot.module.cps.initServer.emqx.handler.ActionHandler;
import cn.snowsoft.iot.module.cps.initServer.emqx.handler.AuthenticationHandler;
import cn.snowsoft.iot.module.cps.initServer.emqx.handler.BaseHandler;
import cn.snowsoft.iot.module.cps.initServer.emqx.handler.ConnectorHandler;
import cn.snowsoft.iot.module.cps.initServer.emqx.handler.RuleHandler;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 在服务启动时根据现有数据库相关信息进行emqx的规则自动化新增或更新
 */
@Component
@DependsOn({"emqxProperties"})
public class InitMqttService {

    @Autowired
    private EmqxProperties emqxProperties;

    /**
     * 在服务启动时进行 内置数据库的认证器创建，默认规则的创建初始化
     */
    @PostConstruct
    public void process() {
        //认证器的处理器
        BaseHandler authenticationHandler = new AuthenticationHandler();
        //创建emqx连接器的处理器
        BaseHandler connectorHandler = new ConnectorHandler();
        //创建emqx输出动作的处理器
        BaseHandler actionHandler = new ActionHandler();
        //创建emqx规则sql的处理器
        BaseHandler ruleHandler = new RuleHandler();
        //设置处理器的顺序，构建为一个处理链
        actionHandler.setNextHandler(ruleHandler);
        connectorHandler.setNextHandler(actionHandler);
        authenticationHandler.setNextHandler(connectorHandler);
        //开启处理链的执行
        authenticationHandler.handleRequest(emqxProperties);
    }
}
