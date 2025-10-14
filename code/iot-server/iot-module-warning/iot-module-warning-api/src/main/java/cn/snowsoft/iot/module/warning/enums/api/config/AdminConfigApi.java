package cn.snowsoft.iot.module.warning.enums.api.config;

import cn.snowsoft.iot.module.warning.enums.ApiConstants;
import cn.snowsoft.iot.module.warning.enums.api.config.vo.ConfigCountVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 管理员告警配置")
public interface AdminConfigApi {

    String PREFIX = ApiConstants.PREFIX + "/configuration";

    /**
     * 通过设备 ID 查询关联告警数
     * @param equipmentCodeList
     * @return
     */
    @PostMapping(PREFIX  + "/get")
    @Operation(summary = "通过设备 ID 查询关联告警数")
    @Parameter(name = "ids", description = "设备codes", example = "1", required = true)
    List<ConfigCountVO> getWarningCount(@RequestBody List<String> equipmentCodeList);

    @GetMapping(PREFIX  + "/deleteByEquipmentId")
    @Operation(summary = "通过设备 ID 删除关联告警")
    @Parameter(name = "id", description = "设备id", example = "1", required = true)
    boolean deleteByEquipmentId(@RequestParam("id") Long id);
}
