package cn.snowsoft.iot.module.warning.event;

import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningDetailVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author: chen_gang
 * @date: 2024/8/12 15:57
 * @description: 告警事件发布对象
 */
@Getter
@RequiredArgsConstructor
@ToString
public class WarningEvent {
    private final JSONObject jsonObject;
    private final String uuId;
    private final Long actionId;
    private final WarningDetailVO warningDetailVO;
    private final ConfigActionDO configActionDO;
}
