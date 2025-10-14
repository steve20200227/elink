package cn.snowsoft.iot.module.warning.event;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.ToString;

/**
 * @author: chen_gang
 * @date: 2024/8/12 16:55
 * @description: mysql持久化数据发布事件类
 */
@Getter
@ToString
public class MysqlWarningEvent extends WarningEvent{

    private String warningType;
    private String warningName;

    public MysqlWarningEvent(JSONObject jsonObject, String uuId, String warningType, String warningName) {
        super(jsonObject, uuId, null,null,null);
        this.warningType = warningType;
        this.warningName = warningName;
    }
}
