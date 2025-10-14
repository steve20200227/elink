package cn.snowsoft.iot.module.system.api.notify;

import cn.snowsoft.iot.framework.common.enums.UserTypeEnum;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.system.api.notify.NotifyMessageSendApi;
import cn.snowsoft.iot.module.system.api.notify.dto.NotifySendMessageUserReqDTO;
import cn.snowsoft.iot.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import cn.snowsoft.iot.module.system.dal.dataobject.notify.NotifyTemplateDO;
import cn.snowsoft.iot.module.system.service.notify.NotifySendService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.snowsoft.iot.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class NotifyMessageSendApiImpl implements NotifyMessageSendApi {

    @Resource
    private NotifySendService notifySendService;

    @Override
    public CommonResult<Long> sendSingleMessageToAdmin(NotifySendSingleToUserReqDTO reqDTO) {
        return success(notifySendService.sendSingleNotifyToAdmin(reqDTO.getUserId(),
                reqDTO.getTemplateCode(), reqDTO.getTemplateParams()));
    }

    @Override
    public CommonResult<Long> sendSingleMessageToMember(NotifySendSingleToUserReqDTO reqDTO) {
        return success(notifySendService.sendSingleNotifyToMember(reqDTO.getUserId(),
                reqDTO.getTemplateCode(), reqDTO.getTemplateParams()));
    }

    @Override
    public CommonResult<Long> sendMessageUser(NotifySendMessageUserReqDTO reqDTO) {
        NotifyTemplateDO notifyTemplateDO = new NotifyTemplateDO();
        notifyTemplateDO.setContent(reqDTO.getTemplateContent());
        notifyTemplateDO.setType(2);
        notifyTemplateDO.setId(reqDTO.getTemplateId());
        notifyTemplateDO.setNickname("管理员");
        Long aLong = notifySendService.sendNotify(reqDTO.getUserId(), UserTypeEnum.ADMIN.getValue(), notifyTemplateDO, reqDTO.getTemplateParams());

        return success(aLong);
    }

}
