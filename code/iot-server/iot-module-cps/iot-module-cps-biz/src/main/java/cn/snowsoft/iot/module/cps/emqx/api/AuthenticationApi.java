package cn.snowsoft.iot.module.cps.emqx.api;

import cn.snowsoft.iot.module.cps.config.SpringContextConfig;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPage;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.emqx.properties.ResponseResult;
import okhttp3.RequestBody;

import java.io.IOException;
import okhttp3.*;
/**
 * emqx 认证器操作类,主要操作认证器内部的用户管理
 *
 * @author Chill
 */
public class AuthenticationApi {

    /**
     * 指定认证器中增加用户
     */
    public static ResponseResult insertAuthentication(String userId, String password) throws Exception{
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getAuthenticationUsersUrl(),
                "{\"user_id\":\"" + userId + "\",\"password\":\"" + password + "\"}", "POST");
    }

    /**
     * 指定认证器中删除用户
     */
    public static ResponseResult removeAuthentication(String userId) throws Exception{
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getAuthenticationUsersUrl() + "/" + userId,
                "", "DELETE");
    }

    /**
     * 创建认证器
     */
    public static ResponseResult initAuthentication(String data) {
        EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
        return BaseApi.requestApi(emqxProperties.getWebUrl() + emqxProperties.getAuthenticationUrl(),
                data, "POST");
    }
}