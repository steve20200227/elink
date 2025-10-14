package cn.snowsoft.iot.module.cps.controller.admin.product;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductSortVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductSortDO;
import cn.snowsoft.iot.module.cps.service.product.ProductSortService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理后台 - 产品点位管理")
@RestController
@RequestMapping("/cps/productPointSort")
public class ProductSortController {

    private final ProductSortService productSortService;

    public ProductSortController(ProductSortService productSortService) {
        this.productSortService = productSortService;
    }

    /**
     * 产品分类树接口
     *
     * @return List<ProductSortVO>
     */
    @GetMapping("/getTree")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<ProductSortVO>> getTree() {
        return CommonResult.success(productSortService.getAllProductSorts());
    }

    /**
     * 树节点保存接口
     *
     * @param productSortVO productSortVO对象
     * @return ProductSortVO
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 2)
    public CommonResult<ProductSortVO> save(@RequestBody ProductSortVO productSortVO) {
        return CommonResult.success(productSortService.saveData(productSortVO));
    }

    /**
     * 删除节点接口
     *
     * @param id id值
     * @return Boolean
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 3)
    public CommonResult<Boolean> remove(@RequestBody Long id) {
        return productSortService.verifyRemove(id);
    }

    /**
     * 根据id查询详情接口
     *
     * @param id id值
     * @return ProductSortVO
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public CommonResult<ProductSortDO> detail(@RequestParam Long id) {
        return CommonResult.success(productSortService.getById(id));
    }

}
