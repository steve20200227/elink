package cn.snowsoft.iot.module.warning.dal.mysql.warning.record;

import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningActionRecordRespVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailReqVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningActionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WarningActionRecordMapper extends BaseMapper<WarningActionRecord> {
    IPage<WarningActionRecordRespVO> selectOutputRecord(IPage<WarningActionRecord> page, @Param("warningRecordDetailReqVO") WarningRecordDetailReqVO warningRecordDetailReqVO);

    List<MessageRecordRespDto> getMessageRecordDetailList(@Param("warningTagList") List<String> warningTagList);
}
