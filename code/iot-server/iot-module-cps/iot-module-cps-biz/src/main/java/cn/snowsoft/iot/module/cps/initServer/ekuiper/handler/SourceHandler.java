package cn.snowsoft.iot.module.cps.initServer.ekuiper.handler;

import cn.snowsoft.iot.module.cps.initServer.ekuiper.EkuiperProperties;
import cn.snowsoft.iot.module.cps.utils.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

@Slf4j
public class SourceHandler extends BaseHandler {

    /**
     * ekuiper源配置文件初始化
     * @param ekuiperProperties
     */
    @Override
    public void handleRequest(EkuiperProperties ekuiperProperties) {
        List<EkuiperProperties.Source> sources = ekuiperProperties.getSources();
        // 如果需要创建的源配置不为空，则循环调用创建接口进行创建
        if (ObjectUtils.isNotEmpty(sources)) {
            sources.forEach(source -> {
                // 组装请求参数
                JSONObject data = new JSONObject();
                data.put("server", ekuiperProperties.getServer());
                data.put("username", ekuiperProperties.getUsername());
                data.put("password", ekuiperProperties.getPassword());
                data.put("protocolVersion", ekuiperProperties.getProtocolVersion());
                data.put("qos", source.getQos());
                data.put("insecureSkipVerify", ekuiperProperties.isInsecureSkipVerify());
                data.put("connectionSelector", source.getConnectionSelector());
                // 发起请求，如果失败，则退出程序
                if (!HttpUtil.sendPut(ekuiperProperties.getEkuiperUrl() + ekuiperProperties.getSourcesUrl() + source.getName(), data)) {
                    log.error("ekuiper初始化源配置 [{}] 失败, url：{}, 参数： {}",
                            source.getName(), ekuiperProperties.getEkuiperUrl() + ekuiperProperties.getSourcesUrl() + source.getName(), data.toJSONString());
                    System.exit(1);
                } else {
                    log.info("ekuiper初始化源配置 [{}] 成功, url：{}, 参数： {}",
                            source.getName(), ekuiperProperties.getEkuiperUrl() + ekuiperProperties.getSourcesUrl() + source.getName(), data.toJSONString());
                }
            });
        }
    }
}
