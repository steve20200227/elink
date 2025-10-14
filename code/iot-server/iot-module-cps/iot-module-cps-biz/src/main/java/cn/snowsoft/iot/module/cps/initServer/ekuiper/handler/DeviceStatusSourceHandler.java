package cn.snowsoft.iot.module.cps.initServer.ekuiper.handler;

import cn.snowsoft.iot.module.cps.ekuiperClient.MonitorClient;
import cn.snowsoft.iot.module.cps.initServer.ekuiper.EkuiperProperties;
import cn.snowsoft.iot.module.cps.utils.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

@Slf4j
public class DeviceStatusSourceHandler extends BaseHandler {

    /**
     * 初始化设备的状态源
     * @param ekuiperProperties
     */
    @Override
    public void handleRequest(EkuiperProperties ekuiperProperties) {
        //在流计算引擎中增加设备状态源
        // 首先确定计算源的构建sql片段
        String statusStreamSql = "CREATE STREAM DeviceStatusPush () WITH (DATASOURCE=\"iot/device/status\", CONF_KEY=\"" + ekuiperProperties.getConfigKey() + "\", TYPE=\"mqtt\", SHARED=\"true\", FORMAT=\"json\");";
        JSONObject statusStream = new JSONObject();
        statusStream.put("sql", statusStreamSql);
        // 发起请求，如果失败，则退出程序
        if (!MonitorClient.streamCreation(statusStream, ekuiperProperties.getEkuiperUrl())) {
            log.error("ekuiper初始化设备状态源 [DeviceStatusPush] 失败, 参数： {}", statusStreamSql);
            System.exit(1);
        } else {
            log.info("ekuiper初始化设备状态源 [DeviceStatusPush] 成功, 参数： {}", statusStreamSql);
        }
    }
}
