package cn.snowsoft.iot.module.warning.service.warning.config;

import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningSaveVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IConfigActionService extends IService<ConfigActionDO> {

    boolean updateAction(WarningSaveVO warningSaveVO);

    void deleteByConfigId(Long id);

    List<ConfigActionDO> getActionByConfigId(Long id);
}
