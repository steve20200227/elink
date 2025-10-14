package cn.snowsoft.iot.module.warning.service.warning.record;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.RecordVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.WarningEquipmentVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordDetailReqVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordPageVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo.WarningRecordStatusVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningRecordDO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IWarningRecordService extends IService<WarningRecordDO> {
    IPage<WarningRecordDO> selectRecordPage(WarningRecordPageVO recordPageVO);

    CommonResult<WarningRecordDO> updateStatus(WarningRecordStatusVO recordStatusVO);
    boolean handleWarningRecord(JSONObject jsonObject, Long id, String warningName);
    void handleWarningRecord(JSONObject jsonObject, String warningType, String warningName);
    CommonResult<WarningRecordDO> insertRecord(JSONObject data);

    List<RecordVO> getRecordList();

    List<WarningEquipmentVO> getWarningEquipmentList();

    //查询告警记录动作详情
    List getRecodeActionDetailList(WarningRecordDetailReqVO warningRecordDetailReqVO);
}
