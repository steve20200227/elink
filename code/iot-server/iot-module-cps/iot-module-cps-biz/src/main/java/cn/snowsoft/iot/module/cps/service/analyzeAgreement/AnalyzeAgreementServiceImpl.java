package cn.snowsoft.iot.module.cps.service.analyzeAgreement;

import cn.snowsoft.iot.module.cps.controller.admin.analyzeAgreement.vo.AnalyzeAgreementPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.analyzeAgreement.AnalyzeAgreementDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import cn.snowsoft.iot.module.cps.dal.mysql.analyzeAgreement.AnalyzeAgreementMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeAgreementServiceImpl extends ServiceImpl<AnalyzeAgreementMapper, AnalyzeAgreementDO> implements AnalyzeAgreementService{
    @Override
    public IPage<AnalyzeAgreementDO> selectAnalyzeAgreemente(AnalyzeAgreementPageVO analyzeAgreementDO) {
        IPage<EquipmentPointDO> page = new Page<>(analyzeAgreementDO.getPageNo(), analyzeAgreementDO.getPageSize());
        return baseMapper.selectAnalyzeAgreementePage(page, analyzeAgreementDO);
    }
}
