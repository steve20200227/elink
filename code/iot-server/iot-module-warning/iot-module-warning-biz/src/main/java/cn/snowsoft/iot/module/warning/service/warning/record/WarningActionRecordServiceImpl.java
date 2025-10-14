package cn.snowsoft.iot.module.warning.service.warning.record;

import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningActionRecordRespVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailReqVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningActionRecord;
import cn.snowsoft.iot.module.warning.dal.mysql.warning.record.WarningActionRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningActionRecordServiceImpl extends ServiceImpl<WarningActionRecordMapper, WarningActionRecord> implements IWarningActionRecordService {

    @Autowired
    private WarningActionRecordMapper warningActionRecordMapper;

    @Override
    public WarningActionRecord getOutPutRecodeDetail(WarningRecordDetailReqVO warningRecordDetailReqVO) {
        LambdaQueryWrapper<WarningActionRecord> lambdaQuery =  new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(warningRecordDetailReqVO.getWarningTag())){
            lambdaQuery.eq(WarningActionRecord::getWarningRecordId, warningRecordDetailReqVO.getWarningTag());

        }
        //lambdaQuery.eq(OutPutRecord::getWarningActionId, warningRecordDetailReqVO.getConfigId());
        WarningActionRecord warningActionRecord = baseMapper.selectOne(lambdaQuery);
        return warningActionRecord;
    }

    @Override
    public List<WarningActionRecord> getOutPutRecodeDetailList(WarningRecordDetailReqVO warningRecordDetailReqVO) {
        LambdaQueryWrapper<WarningActionRecord> lambdaQuery =  new LambdaQueryWrapper<>();

        if (ObjectUtils.isNotEmpty(warningRecordDetailReqVO.getWarningTagList())){
            lambdaQuery.in(WarningActionRecord::getWarningRecordId, warningRecordDetailReqVO.getWarningTagList());
        }
        if (StringUtils.isNotEmpty(warningRecordDetailReqVO.getActionType())){
            lambdaQuery.eq(WarningActionRecord::getActionType, warningRecordDetailReqVO.getActionType());
        }
        lambdaQuery.orderByAsc(WarningActionRecord::getCreateTime);
        List<WarningActionRecord> warningActionRecord = baseMapper.selectList(lambdaQuery);
        return warningActionRecord;
    }

    @Override
    public List<MessageRecordRespDto> getMessageRecordDetailList(List<String> warningTagList) {
        return baseMapper.getMessageRecordDetailList(warningTagList);
    }

    @Override
    public void saveOutPutRecord(WarningActionRecord warningActionRecord) {
        baseMapper.insert(warningActionRecord);
    }

    @Override
    public IPage<WarningActionRecordRespVO> selectPage(WarningRecordDetailReqVO warningRecordDetailReqVO) {
        Page page = new Page(warningRecordDetailReqVO.getPageNo(),warningRecordDetailReqVO.getPageSize());

        IPage<WarningActionRecordRespVO> page1 = warningActionRecordMapper.selectOutputRecord(page,warningRecordDetailReqVO);
//        Page page1 = baseMapper.selectPage(page, lambdaQuery);
        return page1;
    }
}
