package cn.snowsoft.iot.module.cps.initServer.ekuiper.handler;

import cn.snowsoft.iot.module.cps.initServer.ekuiper.EkuiperProperties;
import cn.snowsoft.iot.module.cps.utils.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

@Slf4j
public class ConnectorHandler extends BaseHandler {

    /**
     * ekuiper连接器初始化
     * @param ekuiperProperties
     */
    @Override
    public void handleRequest(EkuiperProperties ekuiperProperties) {
        List<String> connectors = ekuiperProperties.getConnectionNames();
        // 如果需要创建的连接器不为空，则循环调用创建接口进行创建
        if (ObjectUtils.isNotEmpty(connectors)) {
            connectors.forEach(connector -> {
                // 组装请求参数
                JSONObject data = new JSONObject();
                data.put("server", ekuiperProperties.getServer());
                data.put("username", ekuiperProperties.getUsername());
                data.put("password", ekuiperProperties.getPassword());
                data.put("protocolVersion", ekuiperProperties.getProtocolVersion());
                data.put("insecureSkipVerify", ekuiperProperties.isInsecureSkipVerify());
                // 发起请求，如果失败，则退出程序
                if (!HttpUtil.sendPut(ekuiperProperties.getEkuiperUrl() + ekuiperProperties.getConnectionsUrl() + connector, data)) {
                    log.error("ekuiper初始化连接器 [{}] 失败, url：{}, 参数： {}",
                            connector, ekuiperProperties.getEkuiperUrl() + ekuiperProperties.getConnectionsUrl() + connector, data.toJSONString());
                    System.exit(1);
                } else {
                    log.info("ekuiper初始化连接器 [{}] 成功, url：{}, 参数： {}",
                            connector, ekuiperProperties.getEkuiperUrl() + ekuiperProperties.getConnectionsUrl() + connector, data.toJSONString());
                }
            });
        }
        //继续执行下一个处理器
        nextHandler.handleRequest(ekuiperProperties);
    }
}
