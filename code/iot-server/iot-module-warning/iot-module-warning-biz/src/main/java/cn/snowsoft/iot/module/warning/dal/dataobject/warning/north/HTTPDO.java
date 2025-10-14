package cn.snowsoft.iot.module.warning.dal.dataobject.warning.north;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class HTTPDO {

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 请求方法
     */
    private String requestMethod;

    private String requestBody;

    private List<HashMap<String, Object>> requestParams;

    private List<HashMap<String, Object>> requestHeaders;
}
