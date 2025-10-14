package cn.snowsoft.iot.module.cps.controller.admin.product;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPointPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductPointDO;
import cn.snowsoft.iot.module.cps.service.product.ProductPointService;
import cn.snowsoft.iot.module.cps.utils.StringToList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "管理后台 - 产品点位管理")
@RestController
@RequestMapping("/cps/productPoint")
public class ProductPointController {

    @Resource
    private ProductPointService pointService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    public CommonResult<ProductPointDO> detail(Long id) {
        return CommonResult.success(pointService.getById(id));
    }

    /**
     * 查询自定义分页
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 2)
    public CommonResult<IPage<ProductPointDO>> page(ProductPointPageVO point) {
        IPage<ProductPointDO> pages = pointService.selectPointPage(point);
        return CommonResult.success(pages);
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    public CommonResult<List<ProductPointDO>> list(ProductPointPageVO point) {
        return CommonResult.success(pointService.lambdaQuery().eq(ProductPointDO::getProductId, point.getProductId()).eq(ProductPointDO::getPointType, point.getPointType()).list());
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 3)
    public CommonResult submit(@RequestBody ProductPointDO point) {
        return pointService.submit(point);
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 4)
    public CommonResult<Boolean> remove( @RequestParam String ids) {
        List<Long> longList = StringToList.toLongList(ids);
        return CommonResult.success(pointService.deleteLogic(longList));
    }

    /**
     * 根据设备id查询设备功能模型
     */
    @PostMapping("/getByProductId")
    @ApiOperationSupport(order = 5)
    public CommonResult<List<ProductPointDO>> getByProductId(@RequestParam Long productId) {
        return CommonResult.success(pointService.getByProductId(productId));
    }
}
