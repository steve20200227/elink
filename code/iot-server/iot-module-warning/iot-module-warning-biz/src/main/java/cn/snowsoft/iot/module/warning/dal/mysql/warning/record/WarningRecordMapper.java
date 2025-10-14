package cn.snowsoft.iot.module.warning.dal.mysql.warning.record;

import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.RecordVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.WarningEquipmentVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailReqVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailRespVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordPageVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningRecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WarningRecordMapper extends BaseMapper<WarningRecordDO> {
    IPage<WarningRecordDO> selectWarningPage(IPage<WarningRecordDO> page, @Param("entity") WarningRecordPageVO recordPageVO);

    List<RecordVO> getRecordList();

    List<WarningEquipmentVO> getWarningEquipmentList();

    List<WarningRecordDetailRespVO> getRecordActionDetailList(@Param("warningRecord") WarningRecordDetailReqVO warningRecordDetailReqVO);
}
