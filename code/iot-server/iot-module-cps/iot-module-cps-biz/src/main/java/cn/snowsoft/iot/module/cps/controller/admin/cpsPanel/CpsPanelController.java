package cn.snowsoft.iot.module.cps.controller.admin.cpsPanel;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.cpsPanel.vo.CpsPanelVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import cn.snowsoft.iot.module.cps.service.equipment.EquipmentService;
import cn.snowsoft.iot.module.cps.service.product.ProductService;
import cn.snowsoft.iot.module.warning.enums.api.config.AdminConfigApi;
import cn.snowsoft.iot.module.warning.enums.api.config.vo.ConfigCountVO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/cps/cpsPanel")
@Tag(name = "管理后台 - 采集设备管理接口")
public class CpsPanelController {

    private final EquipmentService equipmentService;

    private final ProductService productService;

    private final AdminConfigApi adminConfigApi;

    /**
     * 根据协议分类获取设备信息
     *
     * @return Map<String, List < EquipmentDO>>对象
     */
    @GetMapping("/getAgreementTypeData")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<CpsPanelVO>> getAgreementTypeData() {
        Map<String, Long> countByAgreementType = equipmentService.list().stream()
                .collect(Collectors.groupingBy(EquipmentDO::getAgreementType, Collectors.counting()));
        List<CpsPanelVO> collect = countByAgreementType.entrySet().stream()
                .map(e -> {
                    return new CpsPanelVO().setName(e.getKey()).setValue(Math.toIntExact(e.getValue()));
                }).collect(Collectors.toList());
        return CommonResult.success(collect);
    }

    /**
     * 根据设备类型分类获取设备信息
     *
     * @return Map<String, List < EquipmentDO>>对象
     */
    @GetMapping("/getEquipmentTypeData")
    @ApiOperationSupport(order = 2)
    public CommonResult<List<CpsPanelVO>> getEquipmentTypeData() {
        Map<String, Long> countByAgreementType = equipmentService.list().stream()
                .collect(Collectors.groupingBy(EquipmentDO::getEquipmentType, Collectors.counting()));
        List<CpsPanelVO> collect = countByAgreementType.entrySet().stream()
                .map(e -> {
                    return new CpsPanelVO().setName(e.getKey()).setValue(Math.toIntExact(e.getValue()));
                }).collect(Collectors.toList());
        return CommonResult.success(collect);
    }

    /**
     * 获取各产品关联设备数量
     *
     * @return List<CpsPanelVO>对象
     */
    @GetMapping("/getProductRelevanceEquipmentData")
    @ApiOperationSupport(order = 3)
    public CommonResult<List<CpsPanelVO>> getProductRelevanceEquipmentData() {
        List<ProductDO> productList = productService.list();
        List<EquipmentDO> equipmentList = equipmentService.list();
        List<CpsPanelVO> collect = productList.stream().map(item -> {
            long count = equipmentList.stream().filter(e -> e.getProductCode().equals(item.getProductCode())).count();
            return new CpsPanelVO().setName(item.getProductName()).setValue(Math.toIntExact(count));
        }).collect(Collectors.toList());
        List<CpsPanelVO> collect1 = collect.stream().sorted(Comparator.comparing(CpsPanelVO::getValue).reversed()).collect(Collectors.toList());
        return CommonResult.success(collect1);
    }

    /**
     * 获取各设备的报警信息
     *
     * @return List<CpsPanelVO>对象
     */
    @GetMapping("/getEquipmentWarningData")
    @ApiOperationSupport(order = 4)
    public CommonResult<List<CpsPanelVO>> getEquipmentWarningData() {
        List<CpsPanelVO> collect = new ArrayList<>();
        List<EquipmentDO> equipmentList = equipmentService.list();
        if (ObjectUtils.isNotEmpty(equipmentList)) {
            Map<String, EquipmentDO> equipmentMap = equipmentList.stream().collect(Collectors.toMap(EquipmentDO::getEquipmentCode, equipment -> equipment));
            List<String> equipmentCodeList = equipmentList.stream().map(EquipmentDO::getEquipmentCode).collect(Collectors.toList());
            List<ConfigCountVO> warningCount = adminConfigApi.getWarningCount(equipmentCodeList);
            collect = warningCount.stream().map(e -> {
                return new CpsPanelVO().setName(equipmentMap.get(e.getEquipmentCode()).getEquipmentName()).setValue(e.getWarningCount());
            }).collect(Collectors.toList());
        }
        return CommonResult.success(collect);
    }
}
