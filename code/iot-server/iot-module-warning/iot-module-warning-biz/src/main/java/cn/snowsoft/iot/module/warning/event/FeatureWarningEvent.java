package cn.snowsoft.iot.module.warning.event;

import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningDetailVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.ToString;

/**
 * @author: chen_gang
 * @date: 2024/11/12 16:53
 * @description: 设备反控发送事件类
 */
@ToString
@Getter
public class FeatureWarningEvent extends WarningEvent{
    private final String params;
    public FeatureWarningEvent(JSONObject jsonObject, String params, String uuId, Long actionId, WarningDetailVO warningDetailVO, ConfigActionDO configActionDO) {
        super(jsonObject, uuId, actionId,warningDetailVO,configActionDO);
        this.params = params;
    }
}
