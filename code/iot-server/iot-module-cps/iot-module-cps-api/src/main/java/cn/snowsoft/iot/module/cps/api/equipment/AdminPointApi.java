package cn.snowsoft.iot.module.cps.api.equipment;

import cn.snowsoft.iot.module.cps.api.equipment.dto.EquipmentPointDTO;
import cn.snowsoft.iot.module.cps.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 设备点位管理")
public interface AdminPointApi {
    String PREFIX = ApiConstants.PREFIX + "/equipmentPoint";


//    String PREFIX = "/admin-api/cps/equipmentPoint";

    @GetMapping(PREFIX + "/detail")
    @Operation(summary = "通过 ID 查询设备点位")
    @Parameter(name = "id", description = "设备点位id", example = "1", required = true)
    EquipmentPointDTO detail(@RequestParam("id") Long id);

}
