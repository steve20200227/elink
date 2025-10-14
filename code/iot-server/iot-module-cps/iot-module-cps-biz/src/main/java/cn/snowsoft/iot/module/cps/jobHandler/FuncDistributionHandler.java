package cn.snowsoft.iot.module.cps.jobHandler;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import cn.snowsoft.iot.module.cps.initServer.cache.ServerCache;
import cn.snowsoft.iot.module.cps.emqx.client.PahoClient;
import cn.snowsoft.iot.module.cps.service.equipment.EquipmentPointService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FuncDistributionHandler {
    @Autowired
    @Lazy
    private PahoClient pahoClient;
    @Autowired
    private EquipmentPointService equipmentPointService;

    /**
     * todo 平台设备的定时任务回调都会走这个处理器，这里需要进行非阻塞异步处理，否则定时任务大量调用时会导致服务崩溃
     * @return
     */
    @XxlJob("funcDistributionHandler")
    public CommonResult<String> distributionHandler() {
        try {
            //获取定时任务参数，该参数有两种形式。1.场景id，2.设备编码+功能点编码，此处handler只会处理第二种形式
            String[] param = XxlJobHelper.getJobParam().split(",");

            //查询指定编码的功能模型
            LambdaQueryWrapper<EquipmentPointDO> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(EquipmentPointDO::getPointCode, param[1]);
            lambdaQueryWrapper.eq(EquipmentPointDO::getEquipmentId, ServerCache.deviceCodeID.get(param[0]));
            EquipmentPointDO equipmentPointDO = equipmentPointService.getOne(lambdaQueryWrapper);
            if (ObjectUtils.isNotEmpty(equipmentPointDO)) {
                log.info("--------------------设备功能下发：{}-----------------",equipmentPointDO.getPointName());
                //查询设备产品 消息发送topic地址
                pahoClient.publish(ServerCache.deviceCodeFeatureTopic.get(param[0]), equipmentPointDO.getInputParameter());
            }
            return CommonResult.success(param[0]);
        } catch (Exception e) {
            return CommonResult.error(500, e.getMessage());
        }
    }
}
