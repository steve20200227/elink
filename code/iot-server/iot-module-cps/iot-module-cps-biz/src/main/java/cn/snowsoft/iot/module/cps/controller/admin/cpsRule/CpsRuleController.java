package cn.snowsoft.iot.module.cps.controller.admin.cpsRule;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsActionOutputVO;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsDataEntryVO;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsRulePageVO;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsRuleSaveVO;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.DeviceProductVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.cpsRule.CpsRule;
import cn.snowsoft.iot.module.cps.service.cpsRule.CpsRuleService;
import cn.snowsoft.iot.module.cps.service.product.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cps/cpsRule")
@Tag(name = "cps规则 - 规则管理接口")
public class CpsRuleController {
    private final CpsRuleService cpsRuleService;

    /**
     * 保存规则信息，仅对在设备接入认证处的转换配置进行保持，不会调用emqx接口进行创建
     */
    @PostMapping("/saveRuleInformation")
    @ApiOperationSupport(order = 3)
    public CommonResult saveRuleInformation(@RequestBody DeviceProductVO deviceProductVO) {
        cpsRuleService.saveOrUpdateRule(deviceProductVO);
        return CommonResult.success(true);
    }

    /**
     * 根据转换规则id查询详情，设备接入认证处调用
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    public CommonResult<CpsRuleSaveVO> detail(Long id) throws JsonProcessingException {
        CpsRule cpsRule1 = cpsRuleService.lambdaQuery().eq(CpsRule::getId, id).one();
        CpsRuleSaveVO equipmentVO = new CpsRuleSaveVO();
        BeanUtils.copyProperties(cpsRule1, equipmentVO);
        ObjectMapper objectMapper = new ObjectMapper();
        if (ObjectUtils.isNotEmpty(equipmentVO.getRuleInput())) {
            equipmentVO.setCpsDataEntries(objectMapper.readValue(equipmentVO.getRuleInput(), new TypeReference<List<CpsDataEntryVO>>() {
            }));
        }
        if (ObjectUtils.isNotEmpty(equipmentVO.getAction())) {
            equipmentVO.setCpsActionOutputs(objectMapper.readValue(equipmentVO.getAction(), new TypeReference<List<CpsActionOutputVO>>() {
            }));
        }
        return CommonResult.success(equipmentVO);
    }

    /**
     * 查询自定义分页
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 2)
    public CommonResult<IPage<CpsRulePageVO>> page(CpsRulePageVO cpsRulePageVO) {
        IPage<CpsRulePageVO> pages = cpsRuleService.selectcpsRulePage(cpsRulePageVO);
        return CommonResult.success(pages);
    }

    /**
     * 根据转换规则的关联id(即设备id)和类型(即属性、事件、功能)查询规则详情，供接入协议调用
     */
    @GetMapping("/detailByWhere")
    @ApiOperationSupport(order = 1)
    public CommonResult detailByWhere(CpsRule cpsRule) throws JsonProcessingException {
        LambdaQueryWrapper<CpsRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(cpsRule.getRelevanceId())){
            lambdaQueryWrapper.eq(CpsRule::getRelevanceId, cpsRule.getRelevanceId());
        }
        if (ObjectUtils.isNotEmpty(cpsRule.getType())){
            lambdaQueryWrapper.eq(CpsRule::getType, cpsRule.getType());
        }
        CpsRule cpsRuleOne = cpsRuleService.getOne(lambdaQueryWrapper);
        CpsRuleSaveVO cpsRuleSaveVO = new CpsRuleSaveVO();

        if (ObjectUtils.isNotEmpty(cpsRuleOne)){
            BeanUtils.copyProperties(cpsRuleOne, cpsRuleSaveVO);
            ObjectMapper objectMapper = new ObjectMapper();
            if (ObjectUtils.isNotEmpty(cpsRuleSaveVO.getRuleInput())) {
                cpsRuleSaveVO.setCpsDataEntries(objectMapper.readValue(cpsRuleSaveVO.getRuleInput(), new TypeReference<List<CpsDataEntryVO>>() {
                }));
            }
            if (ObjectUtils.isNotEmpty(cpsRuleSaveVO.getAction())) {
                cpsRuleSaveVO.setCpsActionOutputs(objectMapper.readValue(cpsRuleSaveVO.getAction(), new TypeReference<List<CpsActionOutputVO>>() {
                }));
            }
        }
        return CommonResult.success(cpsRuleSaveVO);
    }

}