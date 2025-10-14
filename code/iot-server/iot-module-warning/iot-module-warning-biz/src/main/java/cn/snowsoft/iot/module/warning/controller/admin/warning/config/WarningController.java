package cn.snowsoft.iot.module.warning.controller.admin.warning.config;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningPageVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningSaveVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.WarningDO;
import cn.snowsoft.iot.module.warning.service.warning.config.IWarningService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "管理后台 - 告警管理")
@RestController
@RequestMapping("/warning/configuration")
public class WarningController {

    @Resource
    private IWarningService warningService;


    /**
     * 新增或修改
     *
     * @param warningDO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "新增或修改")
    public CommonResult<WarningDO> submit(@RequestBody WarningSaveVO warningDO) {
        return warningService.saveWarningConfig(warningDO);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "详情")
    public CommonResult<WarningSaveVO> detail(Long id) {
        return warningService.getWarningById(id);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "删除")
    public CommonResult<Boolean> remove(@RequestParam Long id) {
        return CommonResult.success(warningService.deleteWarningConfig(id));
    }

    /**
     * 分页
     *
     * @param warningPageVO
     * @return
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "分页")
    public CommonResult<IPage<WarningDO>> page(WarningPageVO warningPageVO) {
        IPage<WarningDO> page = warningService.selectWarningPage(warningPageVO);
        return CommonResult.success(page);
    }

    /**
     * 更改是否启用
     *
     * @param warningDO
     * @return
     */
    @PostMapping("/updateStatus")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "更改是否启用")
    public CommonResult<WarningDO> updateStatus(@RequestBody WarningDO warningDO) {
        return warningService.updateStatus(warningDO);
    }

    /**
     * 重启
     *
     * @param id
     * @return
     */
    @GetMapping("/restart")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "重启")
    public CommonResult<Boolean> restart(Long id) {
        return warningService.restart(id);
    }

    /**
     * 解绑
     *
     * @param id, equipmentCode
     * @return 布尔值是否解绑成功
     */
    @GetMapping("/untie")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "解绑")
    public CommonResult<Boolean> untie(Long id, String equipmentCode) {
        return CommonResult.success(warningService.untie(id, equipmentCode));
    }

    /**
     * 根据设备编码解绑场景
     * @param equipmentCode
     * @return
     */
    @GetMapping("/equipmentUntie")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "解绑")
    public CommonResult<Boolean> equipmentUntie(String equipmentCode) {
        return warningService.equipmentUntie(equipmentCode);
    }
}
