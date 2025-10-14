package cn.snowsoft.iot.module.cps.controller.admin.equipment;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPointPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentQueryVO;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPointPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductPointDO;
import cn.snowsoft.iot.module.cps.service.equipment.EquipmentPointService;
import cn.snowsoft.iot.module.cps.utils.StringToList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Tag(name = "管理后台 - 设备管理点位信息")
@RestController
@RequestMapping("/cps/equipmentPoint")
public class EquipmentPointController {

    @Resource
    private EquipmentPointService pointService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取详情")
    public CommonResult detail(Long id) {
        return CommonResult.success(pointService.getById(id));
    }

    /**
     * 查询自定义分页
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 2)
    public CommonResult<IPage<EquipmentPointDO>> page(EquipmentPointPageVO point) {
        IPage<EquipmentPointDO> pages = pointService.selectPointPage(point);
        return CommonResult.success(pages);
    }

    /**
     * 查询指定设备的属性和事件列表
     */
    @GetMapping("/getDeviceAttributeAndEvent")
    @ApiOperationSupport(order = 2)
    public CommonResult<Map> page(String equipmentCode) {
        Map<String, List> result = pointService.getDeviceAttributeAndEvent(equipmentCode);
        return CommonResult.success(result);
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 3)
    public CommonResult<Boolean> remove(@RequestParam String ids) {
        return CommonResult.success(pointService.deleteLogic(StringToList.toLongList(ids)));
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 4)
    public CommonResult<EquipmentPointDO> submit(@RequestBody EquipmentPointDO point) {
        return CommonResult.success(pointService.submit(point));
    }

    /**
     * 选择产品后覆盖之前的点位信息
     */
    @PostMapping("/coveragePoint")
    @ApiOperationSupport(order = 5)
    public CommonResult<Boolean> coveragePoint(@RequestParam Long equipmentId, @RequestParam Long productId) {
        return CommonResult.success(pointService.coveragePoint(equipmentId,productId));
    }

    /**
     * 根据设备id查询设备点位信息
     */
    @PostMapping("/getByEquipmentId")
    @ApiOperationSupport(order = 6)
    public CommonResult<List<EquipmentPointDO>> getByEquipmentId(@RequestParam Long equipmentId) {
        return CommonResult.success(pointService.getByEquipmentId(equipmentId));
    }

    @PostMapping("/getByEquipmentIdsAndCode")
    @ApiOperationSupport(order = 7)
    public CommonResult<List<EquipmentPointDO>> getByEquipmentIdsAndCode(@RequestBody EquipmentQueryVO equipmentQueryVO) {
        return CommonResult.success(pointService.getByEquipmentIdsAndCode(equipmentQueryVO));
    }

    /**
     * 查询属性列表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 8)
    public CommonResult<List<EquipmentPointDO>> list(EquipmentPageVO point) {
        List<EquipmentPointDO> list = pointService.getList(point);
        return CommonResult.success(list);
    }
}
