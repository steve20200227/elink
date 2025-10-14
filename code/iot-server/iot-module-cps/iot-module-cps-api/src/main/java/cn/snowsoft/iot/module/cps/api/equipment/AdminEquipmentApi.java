package cn.snowsoft.iot.module.cps.api.equipment;

import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentDTO;
import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentRelateWarningDTO;
import cn.snowsoft.iot.module.cps.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 设备管理")
public interface AdminEquipmentApi {

    String PREFIX = "/cps/equipment";
//    String PREFIX = ApiConstants.PREFIX + "/equipment";

    @GetMapping(PREFIX + "/detail")
    @Operation(summary = "通过 ID 查询设备")
    @Parameter(name = "id", description = "设备id", example = "1", required = true)
    EquipmentDTO detail(@RequestParam("id") Long id);

    @PostMapping(PREFIX + "/getEquipmentDetail")
    @Operation(summary = "通过 IDS 查询设备")
    @Parameter(name = "id", description = "设备id", example = "1", required = true)
    List<EquipmentRelateWarningDTO> getEquipmentDetail(@RequestBody List<EquipmentRelateWarningDTO> equipmentRelateWarningDTOS);
    @PostMapping(PREFIX + "/getEquipmentCode")
    @Operation(summary = "通过设备编码查询设备信息")
    @Parameter(name = "code", description = "设备编码", example = "1", required = true)
    List<EquipmentDTO> getEquipmentCode(@RequestBody List<String> equipmentCode);

}
