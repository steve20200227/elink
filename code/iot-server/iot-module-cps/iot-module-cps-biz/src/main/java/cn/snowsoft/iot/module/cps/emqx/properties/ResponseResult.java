package cn.snowsoft.iot.module.cps.emqx.properties;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ResponseResult {

    private int code;

    private String message;


    //判断是否需要向下执行
    public boolean isSuccess() {
        if (code == 400){
            JSONObject jsonObject = JSONObject.parseObject(message);
            //规则已经存在的情况 需要根据message判断
            if ("ALREADY_EXISTS".equals(jsonObject.get("code"))
                    ||"ALREADY_EXISTS".equals(jsonObject.get("code"))
            ||("BAD_REQUEST".equals(jsonObject.get("code"))
                    && "rule id already exists".equals(jsonObject.get("message")))
            ){
                return true;
            }else {
                return false;
            }
        }
        //规则创建成功为 201
        return code == 200 || code == 201;
    }

}
