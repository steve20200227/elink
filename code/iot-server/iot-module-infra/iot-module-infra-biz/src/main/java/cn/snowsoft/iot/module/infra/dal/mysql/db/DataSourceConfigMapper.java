package cn.snowsoft.iot.module.infra.dal.mysql.db;

import cn.snowsoft.iot.framework.mybatis.core.mapper.BaseMapperX;
import cn.snowsoft.iot.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源配置 Mapper
 *
 * @author chengang
 */
@Mapper
public interface DataSourceConfigMapper extends BaseMapperX<DataSourceConfigDO> {
}
