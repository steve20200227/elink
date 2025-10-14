package cn.snowsoft.iot.module.warning.service.warning.config;

import cn.snowsoft.iot.module.warning.enums.api.config.AdminConfigApi;
import cn.snowsoft.iot.module.warning.enums.api.config.vo.ConfigCountVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class AdminConfigApiImpl implements AdminConfigApi {

    @Resource
    private IWarningService warningService;

    @Override
    public List<ConfigCountVO> getWarningCount(List<String> equipmentCodeList) {
        List<ConfigCountVO> warningCountByEquipmentId = warningService.getWarningCountByEquipmentId(equipmentCodeList);
        return warningCountByEquipmentId;
    }

    @Override
    public boolean deleteByEquipmentId(Long id) {
        boolean status = warningService.deleteByEquipmentId(id);
        return status;
    }
}
