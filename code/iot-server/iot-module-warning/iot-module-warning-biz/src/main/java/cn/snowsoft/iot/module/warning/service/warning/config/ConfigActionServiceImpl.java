package cn.snowsoft.iot.module.warning.service.warning.config;

import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningSaveVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import cn.snowsoft.iot.module.warning.dal.mysql.warning.config.ConfigActionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigActionServiceImpl extends ServiceImpl<ConfigActionMapper, ConfigActionDO> implements IConfigActionService {

    /**
     * 场景执行动作保存
     * @param warningSaveVO
     * @return
     */
    @Override
    public boolean updateAction(WarningSaveVO warningSaveVO) {
        Long id = warningSaveVO.getId();
        if (ObjectUtils.isNotEmpty(id)) {
            baseMapper.deleteByWarningId(id);
        }
        List<ConfigActionDO> actionDOList = warningSaveVO.getActionDOList();
        return saveBatch(actionDOList);
    }

    @Override
    public void deleteByConfigId(Long id) {
        baseMapper.deleteByWarningId(id);
    }

    @Override
    public List<ConfigActionDO> getActionByConfigId(Long id) {
        return baseMapper.selectByConfigId(id);
    }
}
