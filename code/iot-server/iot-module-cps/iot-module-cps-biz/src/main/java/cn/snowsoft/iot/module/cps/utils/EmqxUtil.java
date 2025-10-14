package cn.snowsoft.iot.module.cps.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class EmqxUtil {
    /**
     * @param url      连接路径
     * @param username 用户名
     * @param password 密码
     * @return ResponseEntity
     */
    public ResponseEntity<String> getRequest(String url, String username, String password) throws Exception {
        //鉴权
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();
        //进行get请求emqx
        return restTemplate.getForEntity(url, String.class);
    }

    /**
     * 放入到黑名单中
     *
     * @param url      连接路径
     * @param map      传入的参数
     * @param username 用户名
     * @param password 密码
     * @param topics   topics
     * @return ResponseEntity
     * @throws Exception ResponseEntity
     */
    public ResponseEntity<String> postRequest(String url, Map map, String username, String password, JSONObject topics) throws Exception {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        //鉴权
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();
        //进行post请求emqx
        return restTemplate.postForEntity(url, map, String.class);
    }

    /**
     * 将服务放入从黑名单中移除
     *
     * @param url        url
     * @param iplocation iplocation
     * @param username   username
     * @param password   password
     * @param topics     topics
     * @throws Exception Exception
     */
    public void deleteRequest(String url, String iplocation, String username, String password, JSONObject topics) throws Exception {
        //鉴权
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();
        //进行get请求emqx
        restTemplate.delete(url + iplocation, String.class);
    }

    /**
     * 将服务放入从黑名单中移除
     *
     * @param url      url
     * @param clsid    clsid
     * @param username username
     * @param password password
     * @param topics   topics
     * @throws Exception Exception
     */
    public void deleteClientsIdRequest(String url, String clsid, String username, String password, JSONObject topics) throws Exception {
        //鉴权
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();
        //进行get请求emqx
        restTemplate.delete(url + clsid, String.class);
    }


}
