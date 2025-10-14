package cn.snowsoft.iot.module.cps.controller.admin.analyzeAgreement;

import cn.hutool.core.bean.BeanUtil;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.analyzeAgreement.vo.AnalyzeAgreementPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.analyzeAgreement.AnalyzeAgreementDO;
import cn.snowsoft.iot.module.cps.initServer.cache.ServerCache;
import cn.snowsoft.iot.module.cps.service.analyzeAgreement.AnalyzeAgreementService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "管理后台 - 接入协议")
@RestController
@RequestMapping("/cps/analyzeAgreement")
public class AnalyzeAgreementController {

    private String tcpAddress;

    @Resource
    private AnalyzeAgreementService analyzeAgreementService;


    /**
     * 新增或修改
     *
     * @param analyzeAgreementDO 接入协议
     * @return 接入协议
     */
    @PostMapping("submit")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "新增或修改")
    public CommonResult<AnalyzeAgreementDO> submit(@RequestBody AnalyzeAgreementDO analyzeAgreementDO) {
        if (analyzeAgreementDO.getId() == null && analyzeAgreementDO.getAgreementCode() != null) {
            AnalyzeAgreementDO agreementDO = analyzeAgreementService.lambdaQuery().eq(AnalyzeAgreementDO::getAgreementCode, analyzeAgreementDO.getAgreementCode()).one();
            if (!(BeanUtil.isEmpty(agreementDO))) {
                return CommonResult.error(207, "接入协议编码已存在");
            }
        }
        // 这里传入接入协议地址端口
        if (StringUtils.isNotBlank(analyzeAgreementDO.getAccessAddress())) {
            List<AnalyzeAgreementDO> agreementDOS = analyzeAgreementService.lambdaQuery()
                    .like(AnalyzeAgreementDO::getAccessAddress, analyzeAgreementDO.getAccessAddress())
                    .ne(analyzeAgreementDO.getId() != null, AnalyzeAgreementDO::getId, analyzeAgreementDO.getId())
                    .list();
            if (!agreementDOS.isEmpty()) {
                return CommonResult.error(207, "接入地址端口已存在");
            }
        }
        analyzeAgreementDO.setAccessAddress(tcpAddress + ":" + analyzeAgreementDO.getAccessAddress());
        // 执行插入或更新，如果成功则更新全局协议脚本
        if (analyzeAgreementService.saveOrUpdate(analyzeAgreementDO)) {
            ServerCache.protocolAgreement.put(analyzeAgreementDO.getAgreementCode(), analyzeAgreementDO.getCustomAgreement());
        }
        return CommonResult.success(analyzeAgreementDO);
    }

    /**
     * 全量查询
     *
     * @return 接入协议集合
     */
    @GetMapping("getAnalyzeAgreement")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "查询全量")
    public CommonResult<List<AnalyzeAgreementDO>> getAnalyzeAgreement() {
        List<AnalyzeAgreementDO> list = analyzeAgreementService.list();
        AnalyzeAgreementDO analyzeAgreementDO = new AnalyzeAgreementDO();
        analyzeAgreementDO.setAccessAddress("");
        analyzeAgreementDO.setAgreementName("自定义");
        analyzeAgreementDO.setAgreementCode("CUSTOM");
        list.add(analyzeAgreementDO);
        return CommonResult.success(analyzeAgreementService.list());
    }

    /**
     * 查询自定义分页
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 1)
    public CommonResult<IPage<AnalyzeAgreementDO>> page(AnalyzeAgreementPageVO analyzeAgreementDO) {
        IPage<AnalyzeAgreementDO> pages = analyzeAgreementService.selectAnalyzeAgreemente(analyzeAgreementDO);
        return CommonResult.success(pages);
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 4)
    public CommonResult<Boolean> remove(@RequestParam String id) {
        AnalyzeAgreementDO temp = analyzeAgreementService.getById(id);
        boolean flag = analyzeAgreementService.removeById(id);
        if (flag) {
            ServerCache.protocolAgreement.remove(temp.getAgreementCode());
        }
        return CommonResult.success(flag);
    }
}
