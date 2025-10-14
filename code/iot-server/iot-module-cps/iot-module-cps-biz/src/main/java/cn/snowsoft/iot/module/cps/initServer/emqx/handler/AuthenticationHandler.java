package cn.snowsoft.iot.module.cps.initServer.emqx.handler;

import cn.snowsoft.iot.module.cps.emqx.api.AuthenticationApi;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.emqx.properties.ResponseResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationHandler extends BaseHandler{

    /**
     * Emqx认证器初始化----无则创建，有则跳过
     * @param emqxProperties
     */
    @Override
    public void handleRequest(EmqxProperties emqxProperties) {
        //加密参数
        JSONObject passwordHashAlgorithm = new JSONObject();
        //请求体对象
        JSONObject body = new JSONObject();
        //加密方式
        passwordHashAlgorithm.put("name", "sha256");
        //加密盐位置
        passwordHashAlgorithm.put("salt_position", "suffix");
        //认证方式 -使用客户端用户名、Client ID 与密码进行认证
        body.put("mechanism", "password_based");
        //数据源 -内置数据库
        body.put("backend", "built_in_database");
        //账号类型
        body.put("user_id_type", "username");
        //是否启用
        body.put("enable", true);
        body.put("password_hash_algorithm", passwordHashAlgorithm);
        //发起认证器创建请求
        ResponseResult result = AuthenticationApi.initAuthentication(body.toJSONString());
        switch (result.getCode()) {
            case 200:
                log.info("初始化认证器成功: 参数:{}", body.toJSONString());
                break;
            case 400:
                log.error("初始化认证器失败 请求参数 {} 响应信息:{}", body.toJSONString(), result.getMessage());
                System.exit(0);
            case 409:
                log.info("初始化认证器已存在: 参数:{}", body.toJSONString());
                break;
            default:
                log.error("初始化认证器异常 请求参数 {} 响应信息:{}", body.toJSONString(), result.getMessage());
                System.exit(0);
        }
        // 调用下一个处理器
        nextHandler.handleRequest(emqxProperties);
    }
}
