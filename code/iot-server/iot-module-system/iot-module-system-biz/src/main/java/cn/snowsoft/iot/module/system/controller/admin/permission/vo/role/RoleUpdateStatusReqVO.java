package cn.snowsoft.iot.module.system.controller.admin.permission.vo.role;

import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "管理后台 - 角色创建/更新 Request VO")
@Data
public class RoleUpdateStatusReqVO {

    @Schema(description = "角色编号",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态空")
    @DiffLogField(name = "状态")
    private Integer status;

}
