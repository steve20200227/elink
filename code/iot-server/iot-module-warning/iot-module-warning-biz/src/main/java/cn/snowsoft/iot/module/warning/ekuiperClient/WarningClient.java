package cn.snowsoft.iot.module.warning.ekuiperClient;

import cn.snowsoft.iot.module.warning.utils.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

public class WarningClient {

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
        String response = HttpUtil.sendPut(requestUrl, data);
        return response != null && !response.isEmpty();
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
        String response = HttpUtil.sendPut(requestUrl, data);
        return response != null && !response.isEmpty();
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
    public static boolean ruleValidate(JSONObject data, String url) {
        String requestUrl = url + "/rules/validate";
        String response = HttpUtil.sendPost(requestUrl, data);
        return response != null && !response.isEmpty();
    }


    /**
     * 获取指定连接类型的源配置列表信息
     * @param url      http://192.168.1.249:9081
     * @param type     mqtt
     * @return
     */
    public static String getSourcesInfo(String url, String type) {
        String requestUrl = url + "/metadata/sources/yaml/" + type;
        String response = HttpUtil.sendGet(requestUrl);
        if (response != null && !response.isEmpty()) {
            return response;
        } else {
            return null;
        }
    }


    /**
     * 脚本函数创建或更新
     * 如果 id 的函数不存在，将创建它。否则，将更新它。
     * @param data
     * @return
     */
    public static boolean createOrUpdateJavaScript(JSONObject data, String url) {
        String requestUrl = url + "/udf/javascript/" + data.getString("id");
        String response = HttpUtil.sendPut(requestUrl, data);
        return response != null && !response.isEmpty();
    }

    /**
     * 脚本函数删除
     *
     * @param id
     * @return
     */
    public static boolean deleteJavaScript(String id, String url) {
        String requestUrl = url + "/udf/javascript/" + id;
        return HttpUtil.sendDelete(requestUrl);
    }

}
