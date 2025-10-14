package cn.snowsoft.iot.module.cps.dal.dataobject.eventRecord;

import cn.snowsoft.iot.framework.mybatis.core.dataobject.BaseDO;
import cn.snowsoft.iot.framework.mybatis.core.entity.BaseEntity;
import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value = "cps_event_record", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRecordDO extends BaseDO {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String deviceCode;

    private String eventCode;

    private String payload;

}
