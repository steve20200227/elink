package cn.snowsoft.iot.module.warning.controller.admin.warning.record;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailReqVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordPageVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordStatusVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningRecordDO;
import cn.snowsoft.iot.module.warning.service.warning.record.IWarningRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "管理后台 - 告警记录")
@RestController
@RequestMapping("/warning/record")
public class WarningRecordController {

    @Autowired
    private IWarningRecordService recordService;

    /**
     * 分页
     * @param recordPageVO
     * @return
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "分页")
    public CommonResult<IPage<WarningRecordDO>> page(WarningRecordPageVO recordPageVO) {
        IPage<WarningRecordDO> page = recordService.selectRecordPage(recordPageVO);
        return CommonResult.success(page);
    }

    /**
     * 告警记录
     * @param
     * @return
     */
    @GetMapping("/getRecodeLogList")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "查询告警记录动作详情")
    public CommonResult getRecodeActionDetailList(WarningRecordDetailReqVO warningRecordDetailReqVO) {

        List list = recordService.getRecodeActionDetailList(warningRecordDetailReqVO);

        return CommonResult.success(list);
    }


    /**
     * 更改告警记录状态
     * @return
     */
    @PostMapping("/updateStatus")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "更改告警记录状态")
    public CommonResult<WarningRecordDO> updateStatus(@RequestBody WarningRecordStatusVO recordStatusVO) {
        return recordService.updateStatus(recordStatusVO);
    }

    /**
     * 根据 id 查询记录数据
     * @param id
     * @return
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "根据 id 查询记录数据")
    public CommonResult<WarningRecordDO> detail(@RequestParam Long id) {
        return CommonResult.success(recordService.getById(id));
    }

}
