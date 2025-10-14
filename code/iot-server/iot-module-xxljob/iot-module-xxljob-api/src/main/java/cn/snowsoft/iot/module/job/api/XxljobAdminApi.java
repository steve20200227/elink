package cn.snowsoft.iot.module.job.api;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.job.dto.XxlJobEntity;
import cn.snowsoft.iot.module.job.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.PermitAll;


@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 定时任务")
public interface XxljobAdminApi  {

    String PREFIX = ApiConstants.PREFIX+ "/jobInfo";

    @RequestMapping(PREFIX + "/saveOrUpdate")
    @Operation(summary = "新增/修改任务")
    @Parameter(name = "cron", description = "定时规则", example = "1024", required = true)
    @PermitAll
    CommonResult<String> saveOrUpdate(@RequestBody XxlJobEntity xxlJobEntity);


    @RequestMapping(PREFIX + "/start")
    @Operation(summary = "开启任务")
    @Parameter(name = "id", description = "id", example = "1024", required = true)
    @PermitAll
    CommonResult<String> start(@RequestParam("id") int id);

    @RequestMapping(PREFIX + "/stop")
    @Operation(summary = "结束任务")
    @Parameter(name = "id", description = "id", example = "1024", required = true)
    @PermitAll
    CommonResult<String> stop(@RequestParam("id") int id);

    @RequestMapping(PREFIX + "/remove")
    @Operation(summary = "删除任务")
    @Parameter(name = "id", description = "id", example = "1024", required = true)
    @PermitAll
    CommonResult<String> remove(@RequestParam("id") int id);
}
