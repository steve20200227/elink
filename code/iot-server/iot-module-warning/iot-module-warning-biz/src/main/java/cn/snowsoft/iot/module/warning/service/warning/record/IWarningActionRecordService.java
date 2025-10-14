package cn.snowsoft.iot.module.warning.service.warning.record;

import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningActionRecordRespVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailReqVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningActionRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IWarningActionRecordService extends IService<WarningActionRecord> {
    WarningActionRecord getOutPutRecodeDetail(WarningRecordDetailReqVO warningRecordDetailReqVO);
    List<WarningActionRecord> getOutPutRecodeDetailList(WarningRecordDetailReqVO warningRecordDetailReqVO);
    List<MessageRecordRespDto> getMessageRecordDetailList(List<String> warningTagList);

    void saveOutPutRecord(WarningActionRecord warningActionRecord);

    IPage<WarningActionRecordRespVO> selectPage(WarningRecordDetailReqVO warningRecordDetailReqVO);
}
