package cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningCommonVO {
    private String params;
    private JSONObject jsonObject;
    private String uuId;
    private Long actionId;
    private WarningDetailVO warningDetailVO;
}
