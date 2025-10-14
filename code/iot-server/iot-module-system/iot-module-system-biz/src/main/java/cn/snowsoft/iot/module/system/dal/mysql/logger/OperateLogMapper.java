package cn.snowsoft.iot.module.system.dal.mysql.logger;

import cn.snowsoft.iot.framework.common.pojo.PageResult;
import cn.snowsoft.iot.framework.mybatis.core.mapper.BaseMapperX;
import cn.snowsoft.iot.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.snowsoft.iot.module.system.api.logger.dto.OperateLogPageReqDTO;
import cn.snowsoft.iot.module.system.controller.admin.logger.vo.operatelog.OperateLogPageReqVO;
import cn.snowsoft.iot.module.system.dal.dataobject.logger.OperateLogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogMapper extends BaseMapperX<OperateLogDO> {

    default PageResult<OperateLogDO> selectPage(OperateLogPageReqVO pageReqDTO) {
        return selectPage(pageReqDTO, new LambdaQueryWrapperX<OperateLogDO>()
                .eqIfPresent(OperateLogDO::getUserId, pageReqDTO.getUserId())
                .eqIfPresent(OperateLogDO::getBizId, pageReqDTO.getBizId())
                .likeIfPresent(OperateLogDO::getType, pageReqDTO.getType())
                .likeIfPresent(OperateLogDO::getSubType, pageReqDTO.getSubType())
                .likeIfPresent(OperateLogDO::getAction, pageReqDTO.getAction())
                .betweenIfPresent(OperateLogDO::getCreateTime, pageReqDTO.getCreateTime())
                .orderByDesc(OperateLogDO::getId));
    }

    default PageResult<OperateLogDO> selectPage(OperateLogPageReqDTO pageReqDTO) {
        return selectPage(pageReqDTO, new LambdaQueryWrapperX<OperateLogDO>()
                .eqIfPresent(OperateLogDO::getType, pageReqDTO.getType())
                .eqIfPresent(OperateLogDO::getBizId, pageReqDTO.getBizId())
                .eqIfPresent(OperateLogDO::getUserId, pageReqDTO.getUserId())
                .orderByDesc(OperateLogDO::getId));
    }

}
