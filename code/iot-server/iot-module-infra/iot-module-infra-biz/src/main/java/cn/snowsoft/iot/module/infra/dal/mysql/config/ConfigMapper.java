package cn.snowsoft.iot.module.infra.dal.mysql.config;

import cn.snowsoft.iot.framework.common.pojo.PageResult;
import cn.snowsoft.iot.framework.mybatis.core.mapper.BaseMapperX;
import cn.snowsoft.iot.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.snowsoft.iot.module.infra.controller.admin.config.vo.ConfigPageReqVO;
import cn.snowsoft.iot.module.infra.dal.dataobject.config.ConfigDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfigMapper extends BaseMapperX<ConfigDO> {

    default ConfigDO selectByKey(String key) {
        return selectOne(ConfigDO::getConfigKey, key);
    }

    default PageResult<ConfigDO> selectPage(ConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConfigDO>()
                .likeIfPresent(ConfigDO::getName, reqVO.getName())
                .likeIfPresent(ConfigDO::getConfigKey, reqVO.getKey())
                .eqIfPresent(ConfigDO::getType, reqVO.getType())
                .betweenIfPresent(ConfigDO::getCreateTime, reqVO.getCreateTime()));
    }

}
