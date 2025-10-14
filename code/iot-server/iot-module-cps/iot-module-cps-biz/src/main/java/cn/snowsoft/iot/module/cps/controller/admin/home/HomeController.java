package cn.snowsoft.iot.module.cps.controller.admin.home;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.*;
import cn.snowsoft.iot.module.cps.dal.dataobject.home.HomeDO;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.service.equipment.EquipmentService;
import cn.snowsoft.iot.module.cps.service.product.ProductService;
import cn.snowsoft.iot.module.cps.utils.EmqxUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "管理后台 - 首页")
@RestController
@RequestMapping("/cps/home")
public class HomeController {

    @Resource
    private ProductService productService;

    @Resource
    private EmqxProperties emqxProperties;

    @Resource
    private EquipmentService equipmentService;

    @Resource
    private EmqxUtil emqxUtil;

    /**
     * 获取产品、设备和驱动数量
     * @return
     */
    @GetMapping("/count")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取产品、设备和驱动数量")
    public CommonResult<HomeDO> getCount () {
        HomeDO homeDO = new HomeDO();
        // 获取产品相关数据
        List<ProductStatusVO> product = productService.getIsEnableCount();
        Integer productCount = 0;
        Integer productEnableCount = 0;
        Integer productDisableCount = 0;
        for (ProductStatusVO productStatusVO : product) {
            productCount += productStatusVO.getCount();
            if (productStatusVO.getIsEnable() == 1) {
                productEnableCount += productStatusVO.getCount();
            } else {
                productDisableCount += productStatusVO.getCount();
            }
        }

        // 获取设备总数
        List<EquipmentStatusVO> equipment = equipmentService.getIsEnableCount();
        Integer equipmentCount = 0;
        Integer equipmentEnableCount = 0;
        Integer equipmentDisableCount = 0;
        for (EquipmentStatusVO equipmentStatusVO : equipment) {
            equipmentCount += equipmentStatusVO.getCount();
            if (equipmentStatusVO.getIsEnable() == 1) {
                equipmentEnableCount += equipmentStatusVO.getCount();
            } else {
                equipmentDisableCount += equipmentStatusVO.getCount();
            }
        }

        // 设备根据协议类型分布数据
        List<EquipmentAgreementTypeVO> agreementTypeList = equipmentService.getAgreementTypeCount();
        // 产品关联设备数
        List<ProductAssociationEquipmentVO> productEquipmentList = productService.getEquipmentCount();

        // 数据封装
        homeDO.setProductCount(productCount);
        homeDO.setProductEnabelCount(productEnableCount);
        homeDO.setProductDisableCount(productDisableCount);
        homeDO.setProductREquipmentCount(productEquipmentList);

        homeDO.setEquipmentCount(equipmentCount);
        homeDO.setEquipmentEnableCount(equipmentEnableCount);
        homeDO.setEquipmentDisableCount(equipmentDisableCount);
        homeDO.setEquipmentType(agreementTypeList);

        return CommonResult.success(homeDO);
    }

    @GetMapping("/getEmqxMonitor")
    public CommonResult getEmqxMonitor() {
        try {
            //查询最近100秒内的历史数据
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(emqxProperties.getWebUrl() + emqxProperties.getMonitorUrl())
                    .queryParam("latest", 100);
            ResponseEntity<String> responseEntity = emqxUtil.getRequest(builder.toUriString(),
                    emqxProperties.getApikeyOrApiSecret("apiKey"),
                    emqxProperties.getApikeyOrApiSecret("apiSecret"));
            return CommonResult.success(responseEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
