package cn.snowsoft.iot.module.warning.dal.dataobject.warning.north;

import cn.snowsoft.iot.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warning_north")
public class NorthDO extends BaseDO {

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
     * 北向输出 - 输出类型
     */
    private String outputWay;

    /**
     * 相关参数
     */
    private String relatedParameter;
}
