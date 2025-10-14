package cn.snowsoft.iot.module.warning.service.warning.north;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.ActionTypeCountVO;
import cn.snowsoft.iot.module.warning.controller.admin.north.vo.NorthPageVO;
import cn.snowsoft.iot.module.warning.controller.admin.north.vo.NorthSaveOrUpdateVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.NorthDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface INorthService extends IService<NorthDO> {

    CommonResult saveUpdate(NorthSaveOrUpdateVO action);

    IPage<NorthDO> selectWarningPage(NorthPageVO actionPage);

    Boolean removeAction(List<Long> ids);

    CommonResult getActionById(Long id);


    List<ActionTypeCountVO> getActionTypeCount();

    List<NorthDO> getByIds(String ids);
}
