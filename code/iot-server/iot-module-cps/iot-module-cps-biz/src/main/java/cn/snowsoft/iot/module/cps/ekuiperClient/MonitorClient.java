package cn.snowsoft.iot.module.cps.ekuiperClient;

import cn.snowsoft.iot.module.cps.utils.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonitorClient {
    //流相关操作接口

    /**
     * 流创建
     *
     * @param data
     * @return
     */
    public static boolean streamCreation(JSONObject data, String url) {
        String requestUrl = url + "/streams";
        String response = HttpUtil.sendPost(requestUrl, data);
        return response != null && !response.isEmpty();
    }

    /**
     * 流更新
     *
     * @param data
     * @return
     */
    public static boolean streamUpdate(JSONObject data, String url) {
        String requestUrl = url + "/streams/" + data.getString("id");
        return HttpUtil.sendPut(requestUrl, data);
    }

    /**
     * 流删除
     *
     * @param id
     * @return
     */
    public static boolean streamDelete(String id, String url) {
        String requestUrl = url + "/streams/" + id;
        return HttpUtil.sendDelete(requestUrl);
    }
    // 规则相关操作接口
    /**
     * 规则创建
     *
     * @param data
     * @return
     */
    public static boolean ruleCreation(JSONObject data, String url) {
        log.info("数据监控规则生成 {} === {}", url, data);
        String requestUrl = url + "/rules";
        String response = HttpUtil.sendPost(requestUrl, data);
        return response != null && !response.isEmpty();
    }

    /**
     * 规则更新
     *
     * @param data
     * @return
     */
    public static boolean ruleUpdate(JSONObject data, String url) {
        String requestUrl = url + "/rules/" + data.getString("id");
        return HttpUtil.sendPut(requestUrl, data);
    }

    /**
     * 规则删除
     *
     * @param id
     * @return
     */
    public static boolean ruleDelete(String id, String url) {
        String requestUrl = url + "/rules/" + id;
        return HttpUtil.sendDelete(requestUrl);
    }

    /**
     * 规则启动
     *
     * @param id
     * @return
     */
    public static boolean ruleStart(String id, String url) {
        String requestUrl = url + "/rules/" + id + "/start";
        String response = HttpUtil.sendPost(requestUrl, null);
        return response != null && !response.isEmpty();
    }

    /**
     * 规则停止
     *
     * @param id
     * @return
     */
    public static boolean ruleStop(String id, String url) {
        String requestUrl = url + "/rules/" + id + "/stop";
        String response = HttpUtil.sendPost(requestUrl, null);
        return response != null && !response.isEmpty();
    }

    /**
     * 规则重启
     *
     * @param id
     * @return
     */
    public static boolean ruleRestart(String id, String url) {
        String requestUrl = url + "/rules/" + id + "/restart";
        String response = HttpUtil.sendPost(requestUrl, null);
        return response != null && !response.isEmpty();
    }

    /**
     * 规则校验
     *
     * @param data
     * @return
     */
    public static String ruleValidate(JSONObject data, String url) {
        String requestUrl = url + "/rules/validate";
        String response = HttpUtil.sendPost(requestUrl, data);
        if (response != null && !response.isEmpty()) {
            return response;
        } else {
            return null;
        }
    }

}
