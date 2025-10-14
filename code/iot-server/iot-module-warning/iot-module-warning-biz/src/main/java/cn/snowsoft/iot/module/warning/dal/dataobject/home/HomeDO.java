package cn.snowsoft.iot.module.warning.dal.dataobject.home;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeDO {

    /**
     * 动作配置数量
     */
    private Integer actionCount;

    /**
     * 告警配置数量
     */
    private Integer configCount;

    /**
     * 告警配置启用数
     */
    private Integer configEnableCount;

    /**
     * 告警配置未启用数
     */
    private Integer configDisableCount;

    /**
     * 动作类型数量
     */
    private List<JSONObject> actionType;

//    /**
//     *  规则名称
//     */
//    private List<String> ruleNameList;

    /**
     * 规则使用次数
     */
    private Map<String, Long> useCountMap;

    /**
     * 告警记录数量
     */
    private Integer recordCount;

    /**
     * 七天时间
     */
    private List<String> timeList;

    /**
     * 七天数据
     */
    private List<Integer> warningCountList;

    /**
     * 告警设备名称列表
     */
    private List<String> warningNameList;

    /**
     * 告警总数
     */
    private List<Integer> warningTotalList;

    /**
     * 告警处理
     */
    private List<Integer> warningHandleList;
}
