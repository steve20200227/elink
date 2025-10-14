package cn.snowsoft.iot.module.cps.emqx.api;

import cn.snowsoft.iot.module.cps.config.SpringContextConfig;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.emqx.properties.ResponseResult;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import java.io.IOException;

/**
 * emqx 连接器操作类
 */
public class ConnectorApi {

    /**
     * 根据id进行查询动作
     * @param
     * @throws IOException
     */
    public static ResponseResult createConnector(JSONObject actionCode) {
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getConnectorsUrl(),
                actionCode.toJSONString(), "POST");
    }
}
