package cn.snowsoft.iot.module.warning.controller.admin.north.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class NorthPageVO extends PageParam {

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 动作名称
     */
    private String actionName;

    /**
     * 动作类型
     */
    private String actionType;


    /**
     * 消息模板 id
     */
    private Long messageId;

    /**
     * 消息模板名称
     */
    private String messageName;
}
