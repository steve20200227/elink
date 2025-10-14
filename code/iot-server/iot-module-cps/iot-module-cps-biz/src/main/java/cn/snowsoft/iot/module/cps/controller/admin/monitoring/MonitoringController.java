package cn.snowsoft.iot.module.cps.controller.admin.monitoring;


import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SearchParamVO;
import cn.snowsoft.iot.module.cps.service.monitoring.IMonitoringService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 数采监控
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/cps/monitoring")
@Tag(name = "管理后台 - 数采监控信息接口")

public class MonitoringController {
    private final IMonitoringService monitoringService;

    /**
     *
     * @param searchParamVO 传入开始时间，结束时间
     * @return 返回的数据
     */
    @GetMapping("/selectData")
    @ApiOperationSupport(order = 17)
    public CommonResult selectData(SearchParamVO searchParamVO) throws IOException {
        return CommonResult.success(monitoringService.cardDate(searchParamVO));
    }

    @GetMapping("/selectDataHistoryStatistic")
    @ApiOperationSupport(order = 17)
    @Operation(summary = "今日，本周，本月，开始到结束")
    public CommonResult selectDataHistoryStatistic(SearchParamVO searchParamVO) throws IOException {
        return CommonResult.success(monitoringService.selectDataHistoryStatistic(searchParamVO));
    }
}
