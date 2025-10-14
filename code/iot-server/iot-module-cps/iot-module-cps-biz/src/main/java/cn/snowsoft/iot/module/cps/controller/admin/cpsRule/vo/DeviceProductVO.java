package cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo;

import lombok.Data;

@Data
public class DeviceProductVO {
    //设备或者产品的id
    private Long id;
    //规则管理接口
    private CpsRuleSaveVO cpsRuleSaveVO;
}
