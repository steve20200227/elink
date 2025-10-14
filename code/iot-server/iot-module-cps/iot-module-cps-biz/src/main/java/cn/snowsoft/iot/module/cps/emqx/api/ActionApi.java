package cn.snowsoft.iot.module.cps.emqx.api;

import cn.snowsoft.iot.module.cps.config.SpringContextConfig;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.emqx.properties.ResponseResult;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * emqx 输出动作操作类
 */
public class ActionApi {
    /**
     * 根据id进行查询动作
     * @param
     * @throws IOException
     */
    public static ResponseResult getAction(String actionCode) {
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getActionsUrl() + "/" +actionCode, "", "GET");
    }

    public static ResponseResult deleteAction(String actionCode) {
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getActionsUrl() + "/" + actionCode, "", "DELETE");
    }

    //修改动作
    public static ResponseResult updateAction(Map<String, Object> map, String actionCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getActionsUrl() + "/" + actionCode, objectMapper.writeValueAsString(map), "PUT");
    }

    //创建动作
    public static ResponseResult createAction(JSONObject json) {
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getActionsUrl(), json.toJSONString(), "POST");
    }
}
