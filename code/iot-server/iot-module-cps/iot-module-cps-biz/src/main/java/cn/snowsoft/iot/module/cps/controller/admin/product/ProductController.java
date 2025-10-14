package cn.snowsoft.iot.module.cps.controller.admin.product;

import cn.hutool.core.bean.BeanUtil;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPage;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.cpsRule.CpsRule;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductSortDO;
import cn.snowsoft.iot.module.cps.service.cpsRule.CpsRuleService;
import cn.snowsoft.iot.module.cps.service.product.ProductService;
import cn.snowsoft.iot.module.cps.service.product.ProductSortService;
import cn.snowsoft.iot.module.cps.utils.StringToList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "管理后台 - 产品管理")
@RestController
@RequestMapping("/cps/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private ProductSortService productSortService;
    @Resource
    private CpsRuleService cpsRuleService;


    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "详情")
    public CommonResult<ProductVO> detail(Long id) {
        ProductDO productDO = productService.getById(id);
        ProductSortDO productSortDO = productSortService.getById(productDO.getProductSortId());
        ProductVO productVO = new ProductVO();
        BeanUtil.copyProperties(productDO, productVO);
        productVO.setSortName(productSortDO.getSortName());
        List<CpsRule> cpsRuleList = cpsRuleService.lambdaQuery().eq(CpsRule::getRelevanceId, id).list();
        if(cpsRuleList.size() > 2){
            throw new RuntimeException("产品最多关联两个规则");
        }
        cpsRuleList.stream().forEach(e->{
            if("1".equals(e.getType())){
                productVO.setReportId(e.getId());
            }else if("2".equals(e.getType())){
                productVO.setDeliverAnId(e.getId());
            }
        });
        if (StringUtils.isNotEmpty(productVO.getParentProduct())) {
            productVO.setParentProductDO(productService.getOne(new LambdaQueryWrapper<ProductDO>().eq(ProductDO::getProductCode, productVO.getParentProduct())));
        }
        return CommonResult.success(productVO);
    }

    /**
     * 查询自定义分页
     *
     * @return
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 2)
    public CommonResult<IPage<ProductPage>> page(ProductPageVO pageVO) {
        IPage<ProductPage> pages = productService.selectProductPage(pageVO);
        return CommonResult.success(pages);
    }

    /**
     * 新增或修改
     *
     * @param productDo
     * @return
     */
    @PostMapping("submit")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增或修改")
    public CommonResult<ProductDO> submit(@RequestBody ProductDO productDo) {
        if (productDo.getId() == null && productDo.getProductCode() != null) {
            ProductDO product = productService.lambdaQuery().eq(ProductDO::getProductCode, productDo.getProductCode()).one();
            if (!(BeanUtil.isEmpty(product))) {
                return CommonResult.error(207, "产品编码已存在");
            }
            productDo.setIsEnable(0);
        }
        productService.saveOrUpdate(productDo);
        return CommonResult.success(productDo);
    }

    /**
     * 逻辑删除
     *
     * @param ids
     * @return
     */
    @GetMapping("/remove")
    @ApiOperationSupport(order = 4)
    public CommonResult<Boolean> remove(@RequestParam String ids) {
        return productService.removeProduct(StringToList.toLongList(ids));
    }

    @PostMapping("/addAuthentication")
    @ApiOperationSupport(order = 5)
    public CommonResult addAuthentication(@RequestBody ProductPage productDo) {
        return productService.addAuthentication(productDo);
    }

    @GetMapping("/getProductOptions")
    @ApiOperationSupport(order = 6)
    public CommonResult<List<ProductDO>> getEquipmentOptions() {
        return CommonResult.success(productService.getBaseMapper().selectList(new LambdaQueryWrapper<ProductDO>().eq(ProductDO::getEquipmentType, "网关设备")));
    }

    @GetMapping("/getProductByEnable")
    @ApiOperationSupport(order = 7)
    public CommonResult<List<ProductDO>> getProductByEnable() {
        // 获取所有已启用的产品
        return CommonResult.success(productService.getBaseMapper().selectList(new LambdaQueryWrapper<ProductDO>().eq(ProductDO::getIsEnable, 1)));
    }

//    /**
//     * 删除认证器
//     * @param productDo
//     * @return
//     */
//    @PostMapping("/removeAuthentication")
//    @ApiOperationSupport(order = 8)
//    public CommonResult removeAuthentication(@RequestBody ProductDO productDo) {
//        try {
//            Authentication.removeAuthentication(productDo);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return submit(productDo);
//    }

}
