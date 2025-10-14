package cn.snowsoft.iot.module.system.service.logger;

import cn.snowsoft.iot.framework.common.pojo.PageResult;
import cn.snowsoft.iot.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.snowsoft.iot.module.system.api.logger.dto.OperateLogPageReqDTO;
import cn.snowsoft.iot.module.system.controller.admin.logger.vo.operatelog.OperateLogPageReqVO;
import cn.snowsoft.iot.module.system.dal.dataobject.logger.OperateLogDO;

/**
 * 操作日志 Service 接口
 *
 * @author chengang
 */
public interface OperateLogService {

    /**
     * 记录操作日志
     *
     * @param createReqDTO 创建请求
     */
    void createOperateLog(OperateLogCreateReqDTO createReqDTO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqDTO pageReqVO);

}
