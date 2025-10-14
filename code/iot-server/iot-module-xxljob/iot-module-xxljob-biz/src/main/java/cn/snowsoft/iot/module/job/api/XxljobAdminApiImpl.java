package cn.snowsoft.iot.module.job.api;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.job.core.model.XxlJobGroup;
import cn.snowsoft.iot.module.job.core.model.XxlJobInfo;
import cn.snowsoft.iot.module.job.dao.XxlJobGroupDao;
import cn.snowsoft.iot.module.job.dto.XxlJobEntity;
import cn.snowsoft.iot.module.job.service.XxlJobService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;

import static cn.snowsoft.iot.framework.common.pojo.CommonResult.success;

@RestController
@Validated
public class XxljobAdminApiImpl implements XxljobAdminApi {

    @Resource
    private XxlJobService xxlJobService;

    @Resource
    public XxlJobGroupDao xxlJobGroupDao;

    @Override
    public CommonResult<String> saveOrUpdate(@RequestBody XxlJobEntity xxlJobEntity) {
        //根据appname获取对应的id值
        XxlJobGroup xxlJobGroup = xxlJobGroupDao.loadByAppName(xxlJobEntity.getAppName());
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        //业务传递参数
        xxlJobInfo.setId(xxlJobEntity.getId()); //任务id
        xxlJobInfo.setJobDesc(xxlJobEntity.getJobDesc()); //任务描述
        xxlJobInfo.setScheduleConf(xxlJobEntity.getCron()); //定时任务规则cron
        xxlJobInfo.setExecutorParam(xxlJobEntity.getJobParam()); //任务参数
        xxlJobInfo.setJobGroup(xxlJobGroup.getId()); //执行器组id
        xxlJobInfo.setExecutorHandler(xxlJobEntity.getExecutorHandler()); //执行器名称
        xxlJobInfo.setJobType(xxlJobEntity.getJobType()); //任务类型 设备或场景业务
        xxlJobInfo.setRelevanceId(xxlJobEntity.getRelevanceId()); //关联id 设备或场景记录id
        //默认值
        xxlJobInfo.setAuthor("admin");
        xxlJobInfo.setScheduleType("CRON");
        xxlJobInfo.setGlueType("BEAN");
        xxlJobInfo.setExecutorRouteStrategy("FIRST");
        xxlJobInfo.setMisfireStrategy("DO_NOTHING");
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setExecutorTimeout(0);
        xxlJobInfo.setExecutorFailRetryCount(xxlJobEntity.getExecutorFailRetryCount());

        //已存在则更新，不存在则新增
        return success((xxlJobEntity.getId() > 0 ? xxlJobService.update(xxlJobInfo) : xxlJobService.add(xxlJobInfo)).getContent());
    }

    @Override
    public CommonResult<String> start(int id) {
        return success(xxlJobService.start(id).getContent());
    }

    @Override
    public CommonResult<String> stop(int id) {
        return success(xxlJobService.stop(id).getContent());
    }

    @Override
    public CommonResult<String> remove(int id) {
        return success(xxlJobService.remove(id).getContent());
     }

}
