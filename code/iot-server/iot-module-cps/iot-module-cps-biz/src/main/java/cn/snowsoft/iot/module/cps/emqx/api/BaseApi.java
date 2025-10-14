package cn.snowsoft.iot.module.cps.emqx.api;

import cn.snowsoft.iot.module.cps.config.SpringContextConfig;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.emqx.properties.ResponseResult;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author: chen_gang
 * @date: 2025/1/22 16:20
 * @description: api中通用逻辑的抽离
 */
public interface BaseApi {
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // 使用apikey和apisecret的方式请求接口
    static ResponseResult requestApi(String url, String data, String method, String... token) {
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                //传入token则使用token进行鉴权
                .addHeader("Authorization", token.length > 0 ? "Bearer " + token[0] :
                        Credentials.basic(emqxProperties.getApikeyOrApiSecret("apiKey"), emqxProperties.getApikeyOrApiSecret("apiSecret")));
        //构造请求参数和方式
        switch (method.toUpperCase()) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
                RequestBody postBody = RequestBody.create(data, JSON);
                requestBuilder.post(postBody);
                break;
            case "PUT":
                RequestBody putBody = RequestBody.create(data, JSON);
                requestBuilder.put(putBody);
                break;
            case "DELETE":
                requestBuilder.delete();
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
        Request request = requestBuilder.build();

        ResponseResult responseResult = new ResponseResult();
        // 执行请求
        try  {
            Response response = client.newCall(request).execute();
            responseResult.setCode(response.code());
            responseResult.setMessage(response.body().string());
            return responseResult;
        }catch (Exception e) {
            //处理系统异常
            responseResult.setCode(500);
            responseResult.setMessage(e.getMessage());
        }
        return responseResult;
    }

    // 使用web端的账号密码获取emqxToken
    static ResponseResult requestToken(String url, String data) {
        OkHttpClient client = new OkHttpClient();
        // 创建请求
        RequestBody body = RequestBody.create(data, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Content-Type", "application/json")
                .build();
        ResponseResult responseResult = new ResponseResult();
        // 执行请求
        try  {
            Response response = client.newCall(request).execute();
            responseResult.setCode(response.code());
            responseResult.setMessage(response.body().string());
            return responseResult;
        }catch (Exception e) {
            //处理系统异常
            responseResult.setCode(500);
            responseResult.setMessage(e.getMessage());
        }
        return responseResult;
    }
}
