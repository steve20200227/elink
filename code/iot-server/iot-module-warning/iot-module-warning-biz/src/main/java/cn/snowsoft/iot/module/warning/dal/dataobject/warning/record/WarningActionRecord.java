package cn.snowsoft.iot.module.warning.dal.dataobject.warning.record;

import cn.snowsoft.iot.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warning_action_record")
public class WarningActionRecord extends BaseDO {
    @TableId("id")
    private Long id;

    //状态 成功失败
    private Integer outputStatus;

    //推送地址
    private String pushUrl;

    //推送用户
    private String userName;

    //HTTP请求方式
    private String requestType;

    //重试次数
    private Integer retried;



    private String actionType;

    private String warningRecordId;

    //失败原因
    private String failReason;

    //请求参数（推送的数据）
    private String requestParam;

    //北向输出动作ID
    private Long actionId;


}
