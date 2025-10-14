package cn.snowsoft.iot.module.cps.dal.mysql.analyzeAgreement;

import cn.snowsoft.iot.module.cps.controller.admin.analyzeAgreement.vo.AnalyzeAgreementPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.analyzeAgreement.AnalyzeAgreementDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnalyzeAgreementMapper extends BaseMapper<AnalyzeAgreementDO> {
    IPage<AnalyzeAgreementDO> selectAnalyzeAgreementePage(IPage<EquipmentPointDO> page,@Param("entity") AnalyzeAgreementPageVO analyzeAgreementDO);
}
