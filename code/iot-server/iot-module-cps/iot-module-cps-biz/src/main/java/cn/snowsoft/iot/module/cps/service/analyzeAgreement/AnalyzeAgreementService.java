package cn.snowsoft.iot.module.cps.service.analyzeAgreement;

import cn.snowsoft.iot.module.cps.controller.admin.analyzeAgreement.vo.AnalyzeAgreementPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.analyzeAgreement.AnalyzeAgreementDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AnalyzeAgreementService extends IService<AnalyzeAgreementDO> {
    IPage<AnalyzeAgreementDO> selectAnalyzeAgreemente(AnalyzeAgreementPageVO analyzeAgreementDO);
}
