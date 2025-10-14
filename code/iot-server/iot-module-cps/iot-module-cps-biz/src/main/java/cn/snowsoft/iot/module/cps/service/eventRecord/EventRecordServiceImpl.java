package cn.snowsoft.iot.module.cps.service.eventRecord;

import cn.snowsoft.iot.module.cps.dal.dataobject.eventRecord.EventRecordDO;
import cn.snowsoft.iot.module.cps.dal.mysql.eventRecord.EventRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EventRecordServiceImpl extends ServiceImpl<EventRecordMapper, EventRecordDO> implements EventRecordService {
}
