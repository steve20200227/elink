package cn.snowsoft.iot.module.cps.controller.admin.eventRecord;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPage;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.eventRecord.EventRecordDO;
import cn.snowsoft.iot.module.cps.service.eventRecord.EventRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "管理后台 - 事件记录")
@RestController
@RequestMapping("/cps/eventRecord")
public class EventRecordController {
    @Autowired
    private EventRecordService eventRecordService;
    /**
     * 查询自定义分页
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    public CommonResult list(EventRecordDO eventRecordDO) {
        LambdaQueryWrapper<EventRecordDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (ObjectUtils.isNotEmpty(eventRecordDO.getDeviceCode())){
            lambdaQueryWrapper.eq(EventRecordDO::getDeviceCode, eventRecordDO.getDeviceCode());
        }else {
            lambdaQueryWrapper.eq(EventRecordDO::getDeviceCode, "");
        }
        if (ObjectUtils.isNotEmpty(eventRecordDO.getEventCode())){
            lambdaQueryWrapper.eq(EventRecordDO::getEventCode, eventRecordDO.getEventCode());
        }else {
            lambdaQueryWrapper.eq(EventRecordDO::getEventCode, "");
        }
        lambdaQueryWrapper.orderByDesc(EventRecordDO::getCreateTime);
        List<EventRecordDO> list = eventRecordService.list(lambdaQueryWrapper);
        return CommonResult.success(list);
    }
}
