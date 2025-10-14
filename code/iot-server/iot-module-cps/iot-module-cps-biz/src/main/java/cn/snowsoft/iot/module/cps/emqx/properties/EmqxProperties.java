package cn.snowsoft.iot.module.cps.emqx.properties;

import cn.snowsoft.iot.module.cps.emqx.api.BaseApi;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * emqx的配置信息类
 */
@Component
@Data
@Slf4j
@ConfigurationProperties(prefix = "mqtt")
@DependsOn("springContextConfig")
public class EmqxProperties {
    @Autowired
    private RedisCacheManager redisCacheManager;
    // emqx的apikeyUrl
    private String apikeyUrl;
    // emqx的webUrl
    private String webUrl;
    // emqx的管理员账号
    private String webUserName;
    // emqx的管理员密码
    private String webpassword;
    // emqx的登录url
    private String loginUrl;
    // 消息速率url
    private String monitorUrl;
    // 认证器操作api
    private String authenticationUrl;
    // 向指定认证器操作用户数据
    private String authenticationUsersUrl;
    // 规则apiUrl
    private String ruleUrl;
    // 连接器apiUrl
    private String connectorsUrl;
    // 动作apiUrl
    private String actionsUrl;
    // 历史数据存储sql模板
    private String rulesHistory;
    // 客户端配置
    private Client client;
    //连接器配置集合
    private Connectors connectors;
    //动作列表
    private List<Action> actions;
    //规则列表
    private List<Rule> rules;

    /**
     * 从redis缓存中获取apikey和apisecret，获取失败则进行新建
     */
    public String getApikeyOrApiSecret(String type) {
        //从redis缓存中获取apikey和apisecret，获取失败则进行新建
        String apiKey = redisCacheManager.getCache("emqx").get("apiKey", String.class);
        String apiSecret = redisCacheManager.getCache("emqx").get("apiSecret", String.class);
        if (ObjectUtils.anyNull(apiKey, apiSecret)) {
            //使用web端的管理员账号密码换取token
            ResponseResult responseResult = BaseApi.requestToken(webUrl + loginUrl, "{\"username\":\"" + webUserName + "\",\"password\":\"" + webpassword + "\"}");
            //请求成功
            if (responseResult.isSuccess()) {
                JSONObject res = JSONObject.parseObject(responseResult.getMessage());
                // 如果token值不为空
                if (StringUtils.isNotBlank(res.getString("token"))) {
                    // 删除已经存在的指定名称apikey
                    ResponseResult deleteResult = BaseApi.requestApi(webUrl + apikeyUrl + "/cps-server-key", "", "DELETE", res.getString("token"));
                    // 如果204删除成功或者未找到这个指定的apikey，则都可以继续执行创建步骤
                    if (deleteResult.getCode() == 204 || "NOT_FOUND".equals(JSONObject.parseObject(deleteResult.getMessage()).get("code"))) {
                        // 调用创建接口进行apikey的重新创建
                        JSONObject data = new JSONObject();
                        data.put("name", "cps-server-key");
                        data.put("enable", true);
                        data.put("desc", "cps服务内部调用使用");
                        ResponseResult createResult = BaseApi.requestApi(webUrl + apikeyUrl, data.toJSONString(), "POST", res.getString("token"));
                        if (createResult.getCode() == 200) {
                            //创建成功则赋值apiKey和apiSecret
                            JSONObject apiRes = JSONObject.parseObject(createResult.getMessage());
                            apiKey = apiRes.getString("api_key");
                            apiSecret = apiRes.getString("api_secret");
                            redisCacheManager.getCache("emqx").put("apiKey", apiKey);
                            redisCacheManager.getCache("emqx").put("apiSecret", apiSecret);
                            log.info("创建api密钥 [cps-server-key] 成功 key [{}] secret [{}]", apiKey, apiSecret);
                            return "apiKey".equals(type) ? apiKey : apiSecret;
                        }
                        log.error("创建api密钥 [cps-server-key] 失败  {}", createResult.getMessage());
                        System.exit(1);
                    }
                    log.error("删除旧api密钥 [cps-server-key] 失败  {}", deleteResult.getMessage());
                    System.exit(1);
                }
                log.error("获取emqx Token内容为空 {}", res.toJSONString());
                System.exit(1);
            }
            log.error("获取emqx Token失败 {}", responseResult.getMessage());
            System.exit(1);
        }
        return "apiKey".equals(type) ? apiKey : apiSecret;
    }
}
