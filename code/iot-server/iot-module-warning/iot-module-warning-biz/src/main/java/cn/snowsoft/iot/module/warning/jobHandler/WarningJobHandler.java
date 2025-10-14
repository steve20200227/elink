package cn.snowsoft.iot.module.warning.jobHandler;

import cn.snowsoft.iot.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningCommonVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningDetailVO;
import cn.snowsoft.iot.module.warning.event.WarningEventHandler;
import cn.snowsoft.iot.module.warning.service.warning.config.IWarningService;
import cn.snowsoft.iot.module.warning.utils.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author ztt
 * 定时任务回调的统一入口
 */
@Component
@Slf4j
public class WarningJobHandler {
    //使用事件驱动处理定时场景的动作执行，此类作为定时任务回调的统一入口, 需要使用非阻塞异步处理业务执行

    @Autowired
    private IWarningService warningService;

    @Autowired
    private WarningEventHandler warningEventHandler;

    @Value("${ekuiper.httppushurl}")
    private String httppushurl;


    @XxlJob("warningJobHandler")
    public CommonResult<String> jobHandler() {
        try {
            //获取任务参数
            String param = XxlJobHelper.getJobParam();
            //任务参数是场景记录的id，根据id查询场景详情
            WarningDetailVO warningDetailVO = warningService.selectWarningByWarningId(Long.parseLong(param));

            WarningCommonVO warningCommonVO = new WarningCommonVO();
            warningCommonVO.setWarningDetailVO(warningDetailVO);
            //todo 这里需要构造一个json对象传入，后续考虑加入什么内容
            warningCommonVO.setJsonObject(new JSONObject());
            //发布动作执行事件
            return warningEventHandler.handleCommonAction(warningCommonVO) ? CommonResult.success(param) : CommonResult.error(500, "操作失败");
        } catch (Exception e) {
            return CommonResult.error(500, e.getMessage());
        }
    }
}
