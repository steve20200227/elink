package cn.snowsoft.iot.module.cps.initServer.emqx.handler;

import cn.hutool.core.util.StrUtil;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.emqx.properties.Action;
import cn.snowsoft.iot.module.cps.emqx.properties.ResponseResult;
import cn.snowsoft.iot.module.cps.emqx.api.ActionApi;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class ActionHandler extends BaseHandler{

    /**
     * Emqx规则初始化----输出动作的创建
     * @param emqxProperties
     */
    @Override
    public void handleRequest(EmqxProperties emqxProperties) {
        List<Action> actions = emqxProperties.getActions();
        for (Action action : actions) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(action);
            HashMap hashMap = new HashMap();
            hashMap.put("topic", action.getTopic());
            if (StrUtil.isNotBlank(action.getPayload())) {
                hashMap.put("payload", action.getPayload());
            }
            jsonObject.put("parameters", hashMap);
            jsonObject.remove("topic");
            jsonObject.remove("payload");
            ResponseResult actionResult = ActionApi.createAction(jsonObject);
            boolean success = actionResult.isSuccess();
            //有执行失败的
            if (!success){
                System.out.println("参数为"+jsonObject.toJSONString());
                log.error("创建动作失败:名称:{},失败原因:{},参数:{}", action.getName(),actionResult.getMessage(),jsonObject.toJSONString());
                System.exit(0);
            }else {
                log.info("创建动作成功:名称:{},参数:{}", action.getName(),jsonObject.toJSONString());
            }
        }

        nextHandler.handleRequest(emqxProperties);

    }
}
