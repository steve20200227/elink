package cn.snowsoft.iot.module.message.controller.admin;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;

import cn.snowsoft.iot.module.message.controller.admin.vo.MessageRecordVO;
import cn.snowsoft.iot.module.message.model.entity.MessageRecord;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/message/record")
@RequiredArgsConstructor
public class MessageRecordController {

    private final MessageRecordService messageRecordService;
    /**
     * 分页搜索 消息记录
     *
     * @param
     * @return Page对象
     */
    @GetMapping("/getPageList")
    public CommonResult getPageList(MessageRecordVO messageRecordVO) {
        IPage pageList = messageRecordService.getPageList(messageRecordVO);
        return CommonResult.success(pageList);
    }

    /**
     * 分页搜索 消息记录
     *
     * @param
     * @return Page对象
     */
    @GetMapping("/getList")
    public List<MessageRecord> getList(MessageRecordVO messageRecordVO) {
        List list = messageRecordService.getList(messageRecordVO);
        return list;
    }


    /**
     * 分页搜索 消息记录
     *
     * @param
     * @return Page对象
     */
    @GetMapping("/getDetail")
    public List<MessageRecordRespDto> getDetail(MessageRecordVO messageRecordVO) {
        List<MessageRecordRespDto> messageRecordRespDto = messageRecordService.getDetail(messageRecordVO);
        return messageRecordRespDto;
    }
}
