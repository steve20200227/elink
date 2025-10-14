package cn.snowsoft.iot.module.cps.utils;


import cn.snowsoft.iot.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultUtil<T>  implements Serializable {

    private Integer code;
    private T data;
    private String msg;
    private Integer status;

    public static <T> ResultUtil<T> success(T data) {
        ResultUtil<T> result = new ResultUtil<T>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.status = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = data;
        result.msg = "";
        return result;
    }

//    public static <T> ResultUtil<T> success(T data, String msg) {
//        ResultUtil<T> result = new ResultUtil<T>();
//        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
//        result.status = ;
//        result.data = data;
//        result.msg = msg;
//        return result;
//    }

    public static <T> ResultUtil<T> success(String msg) {
        ResultUtil<T> result = new ResultUtil<T>();
        result.status = 207;
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = null;
        result.msg = msg;
        return result;
    }
}
