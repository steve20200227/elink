package cn.snowsoft.iot.module.cps.emqx.api;

import cn.snowsoft.iot.module.cps.config.SpringContextConfig;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.emqx.properties.ResponseResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.Map;

/**
 * EMQX 规则操作类
 */
@Slf4j
public class RuleApi {
    /**
     * 创建规则
     * 此方法在两个业务处进行调用，
     * 1.服务启动时用于处理在emqx规则功能中创建平台依赖的默认规则列表，具体规则列表信息配置在nacos中
     * 2.设备启用、失效过程中进行调用，用于对设备接入认证中的转换功能进行配置
     * @param jsonObject
     * @return
     */
    public static ResponseResult createRule(JSONObject jsonObject) {
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getRuleUrl(),
                jsonObject.toJSONString(), "POST");
    }

    /**
     * 根据id进行查询是否有规则
     * @param ruleCode
     * @throws IOException
     */
    public static ResponseResult getRule(String ruleCode) throws IOException {
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getRuleUrl() + "/" +ruleCode,
                "", "GET");
    }

    public static ResponseResult deleteRules(String rulesCode) throws IOException {
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getRuleUrl() + "/" + rulesCode,
                "", "DELETE");
    }

    /**
     * 修改rule
     * 主要在设备启用、失效过程中进行调用，用于动态更新设备持久化规则
     */
    public static ResponseResult updateRules(Map<String, Object> map,String ruleId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getRuleUrl() + "/" + ruleId,
                objectMapper.writeValueAsString(map), "PUT");
    }
}