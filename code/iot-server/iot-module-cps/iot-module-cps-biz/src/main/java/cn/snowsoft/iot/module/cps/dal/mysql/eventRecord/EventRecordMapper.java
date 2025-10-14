package cn.snowsoft.iot.module.cps.dal.mysql.eventRecord;

import cn.snowsoft.iot.module.cps.dal.dataobject.eventRecord.EventRecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventRecordMapper extends BaseMapper<EventRecordDO> {
}
