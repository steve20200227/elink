package cn.snowsoft.iot.module.warning.event;

import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningDetailVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.ToString;

/**
 * @author: chen_gang
 * @date: 2024/8/12 16:54
 * @description: http发送请求事件类
 */
@ToString
@Getter
public class HttpWarningEvent extends WarningEvent {
    private String params;
    public HttpWarningEvent(JSONObject jsonObject, String params, String uuId, Long actionId, WarningDetailVO warningDetailVO, ConfigActionDO configActionDO) {
        super(jsonObject, uuId, actionId,warningDetailVO,configActionDO);
        this.params = params;
    }
}
