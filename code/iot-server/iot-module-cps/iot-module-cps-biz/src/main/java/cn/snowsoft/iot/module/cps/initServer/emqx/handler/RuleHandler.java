package cn.snowsoft.iot.module.cps.initServer.emqx.handler;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.snowsoft.iot.module.cps.initServer.cache.ServerCache;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.emqx.properties.ResponseResult;
import cn.snowsoft.iot.module.cps.emqx.properties.Rule;
import cn.snowsoft.iot.module.cps.emqx.api.RuleApi;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@Slf4j
public class RuleHandler extends BaseHandler{

    /**
     * Emqx规则初始化----规则sql的创建
     * @param emqxProperties
     */
    @Override
    public void handleRequest(EmqxProperties emqxProperties) {
        List<Rule> rules = emqxProperties.getRules();
        for (Rule rule : rules) {
            //替换有参数的sql
            String sql = buildRuledSql(rule.getSql());
            rule.setSql(sql);
            rule.setId(rule.getName());
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(rule);
            ResponseResult ruleResult = RuleApi.createRule(jsonObject);
            //有执行失败的
            if (!ruleResult.isSuccess()){

                log.error("创建规则失败:名称:{},失败原因:{},失败参数:{}", rule.getName(),ruleResult.getMessage(),jsonObject.toJSONString());
                System.exit(0);
            }else {
                log.info("创建规则成功:名称:{},参数:{}", rule.getName(),jsonObject.toJSONString());
            }
        }
    }

    //构建历史数据规则sql
    public String buildRuledSql(String sql){
        ConcurrentHashSet<String> historySet = ServerCache.historySet;
        StringBuilder sb = new StringBuilder("");
        //计数器
        int count = 0;
        for (String s : historySet) {
            count++;
            //如果是最后一个元素 不需要逗号
            if (count != historySet.size()){
                sb.append("'"+s+"',");
            }else {
                sb.append("'"+s+"'");
            }
        }
        String sqlResult = sql.replace("@{params}",sb.toString());
        return sqlResult;
    }
}
