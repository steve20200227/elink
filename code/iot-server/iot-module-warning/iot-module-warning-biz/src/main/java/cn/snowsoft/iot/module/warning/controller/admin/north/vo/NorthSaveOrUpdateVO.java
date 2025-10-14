package cn.snowsoft.iot.module.warning.controller.admin.north.vo;

import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.HTTPDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.MQTTDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.NorthDO;
import lombok.Data;

@Data
public class NorthSaveOrUpdateVO extends NorthDO {

    private MQTTDO mqtt;

    private HTTPDO http;

    private Object message;
}
