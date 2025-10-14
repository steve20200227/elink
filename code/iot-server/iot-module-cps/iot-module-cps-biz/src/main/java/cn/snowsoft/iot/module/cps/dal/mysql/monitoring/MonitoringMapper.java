package cn.snowsoft.iot.module.cps.dal.mysql.monitoring;


import cn.snowsoft.iot.framework.mybatis.core.mapper.BaseMapperX;
import cn.snowsoft.iot.module.cps.dal.dataobject.monitoring.Monitoring;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数采监控 MonitoringMapper
 */
@Mapper
public interface MonitoringMapper extends BaseMapperX<Monitoring> {
    /**
     * @param page       page
     * @param monitoring monitoring
     * @return List
     */
    List<Monitoring> selectMonitoringPage(IPage<Monitoring> page, @Param("monitoring") Monitoring monitoring);

}
