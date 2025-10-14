package cn.snowsoft.iot.module.warning.controller.admin.north;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.warning.controller.admin.north.vo.NorthPageVO;
import cn.snowsoft.iot.module.warning.controller.admin.north.vo.NorthSaveOrUpdateVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.HTTPDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.MQTTDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.NorthDO;
import cn.snowsoft.iot.module.warning.mqtt.MqttClientUtil;
import cn.snowsoft.iot.module.warning.service.warning.north.INorthService;
import cn.snowsoft.iot.module.warning.utils.ActionHttpUtil;
import cn.snowsoft.iot.module.warning.utils.StringToList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Tag(name = "管理后台 - 北向输出")
@RestController
@RequestMapping("/warning/north")
public class NorthController {

    @Resource
    private INorthService actionService;


    /**
     * 新增或修改
     * @param action
     * @return
     */
    @PostMapping("submit")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "新增或修改")
    public CommonResult submit(@RequestBody NorthSaveOrUpdateVO action) {
        return actionService.saveUpdate(action);
    }

    /**
     * 分页
     * @param actionPage
     * @return
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分页")
    public CommonResult<IPage<NorthDO>> page(NorthPageVO actionPage) {
        IPage<NorthDO> page = actionService.selectWarningPage(actionPage);
        return CommonResult.success(page);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "详情")
    public CommonResult detail (Long id) {
        return actionService.getActionById(id);
    }

    /**
     * 逻辑删除
     * @param ids
     * @return
     */
    @GetMapping("/remove")
    @ApiOperationSupport(order = 4)
    public CommonResult<Boolean> remove(@RequestParam String ids) {
        return CommonResult.success(actionService.removeAction(StringToList.toLongList(ids)));
    }

    /**
     * 测试连接 - MQTT
     * @return
     */
    @PostMapping("/testMQTTConnection")
    @ApiOperationSupport(order = 5)
    public CommonResult testConnection(@RequestBody MQTTDO mqttdo) {
        //根据传入的参数进行连接测试
        MqttClientUtil temp = new MqttClientUtil(
                StringUtils.isBlank(mqttdo.getClsId()) ? UUID.randomUUID().toString().substring(18) : mqttdo.getClsId(),
                mqttdo.getUserName(),
                mqttdo.getPassWord(),
                mqttdo.getMqttAddress());
        if (temp.connect()) {
            temp.closeConnect(1000);
            return CommonResult.success("连接成功");
        } else {
            return CommonResult.error(400, "连接参数错误，请检查");
        }
    }


    /**
     * 测试连接 - HTTP
     * @return
     */
    @PostMapping("/testHTTPConnection")
    @ApiOperationSupport(order = 5)
    public CommonResult testHTTPConnection(@RequestBody HTTPDO httpdo) {
        String s = ActionHttpUtil.testHttp(httpdo);
        return CommonResult.success(s);
    }


    /**
     * 根据ids获取对应数据
     */
    @PostMapping("/getByIds")
    public CommonResult<List<NorthDO>> getByIds(@RequestParam("ids") String ids) {
        return CommonResult.success(actionService.getByIds(ids));
    }
}
