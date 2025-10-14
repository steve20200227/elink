package cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class WarningRecordStatusVO {

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 告警配置 id
     */
    private Long configId;


    /**
     * 告警状态
     */
    private Integer status;

    /**
     * 处理用户
     */
    private String handleUser;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理说明
     */
    private String remark;
}
