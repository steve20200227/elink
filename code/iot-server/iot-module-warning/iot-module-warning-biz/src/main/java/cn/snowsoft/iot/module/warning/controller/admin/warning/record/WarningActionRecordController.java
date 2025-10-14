package cn.snowsoft.iot.module.warning.controller.admin.warning.record;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningActionRecordRespVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailReqVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningActionRecord;
import cn.snowsoft.iot.module.warning.service.warning.record.IWarningActionRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "管理后台 - 北向输出记录")
@RestController
@RequestMapping("/warning/warningActionRecord")
public class WarningActionRecordController {
    @Autowired
    private IWarningActionRecordService recordService;

    /**
     * 告警记录
     * @param
     * @return
     */
    /*@GetMapping("/getOutPutRecodeDetail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "查询北向输出记录动作详情")
    public CommonResult getOutPutRecodeDetail(WarningRecordDetailReqVO warningRecordDetailReqVO) {


        OutPutRecord outPutRecodeDetail = recordService.getOutPutRecodeDetail(warningRecordDetailReqVO);

        return CommonResult.success(outPutRecodeDetail);
    }*/

    @PostMapping("/saveOutPutRecord")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "新增告警记录")
    public CommonResult saveOutPutRecord(@RequestBody WarningActionRecord warningActionRecord) {

        recordService.saveOutPutRecord(warningActionRecord);

        return CommonResult.success("保存成功");
    }

     @GetMapping("/selectPage")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "查询北向输出记录")
    public CommonResult selectPage(WarningRecordDetailReqVO warningRecordDetailReqVO) {


         IPage<WarningActionRecordRespVO> outPutRecordIPage = recordService.selectPage(warningRecordDetailReqVO);

         return CommonResult.success(outPutRecordIPage);
    }
}
