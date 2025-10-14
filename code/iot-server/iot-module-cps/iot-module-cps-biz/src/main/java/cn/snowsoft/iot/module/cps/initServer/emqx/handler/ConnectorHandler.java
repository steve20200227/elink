package cn.snowsoft.iot.module.cps.initServer.emqx.handler;

import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.emqx.properties.Connectors;
import cn.snowsoft.iot.module.cps.emqx.properties.ResponseResult;
import cn.snowsoft.iot.module.cps.emqx.api.ConnectorApi;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class ConnectorHandler extends BaseHandler {

    /**
     * Emqx规则初始化----连接器的创建
     * @param emqxProperties
     */
    @Override
    public void handleRequest(EmqxProperties emqxProperties) {
        Connectors connectors = emqxProperties.getConnectors();
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(connectors);
        ResponseResult connector = ConnectorApi.createConnector(jsonObject);
        //失败
        if (!connector.isSuccess()){
            log.error("初始化连接器失败:名称:{},失败原因:{},失败参数:{}",connectors.getName(),connector.getMessage(),jsonObject.toJSONString());
            System.exit(0);

        }
        log.info("初始化连接器完成:名称:{},参数:{}",connectors.getName(),jsonObject.toJSONString());
        nextHandler.handleRequest(emqxProperties);
    }


}
