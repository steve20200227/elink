package cn.snowsoft.iot.module.system.api.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.framework.common.util.object.BeanUtils;
import cn.snowsoft.iot.module.system.api.permission.PermissionApi;
import cn.snowsoft.iot.module.system.api.user.AdminUserApi;
import cn.snowsoft.iot.module.system.api.user.dto.AdminUserRespDTO;
import cn.snowsoft.iot.module.system.dal.dataobject.dept.DeptDO;
import cn.snowsoft.iot.module.system.dal.dataobject.user.AdminUserDO;
import cn.snowsoft.iot.module.system.service.dept.DeptService;
import cn.snowsoft.iot.module.system.service.user.AdminUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static cn.snowsoft.iot.framework.common.pojo.CommonResult.success;
import static cn.snowsoft.iot.framework.common.util.collection.CollectionUtils.convertSet;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class AdminUserApiImpl implements AdminUserApi {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;
    @Resource
    private PermissionApi permissionApi;

    @Override
    public CommonResult<AdminUserRespDTO> getUser(Long id) {
        AdminUserDO user = userService.getUser(id);
        return success(BeanUtils.toBean(user, AdminUserRespDTO.class));
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUserListBySubordinate(Long id) {
        // 1.1 获取用户负责的部门
        AdminUserDO user = userService.getUser(id);
        if (user == null) {
            return success(Collections.emptyList());
        }
        ArrayList<Long> deptIds = new ArrayList<>();
        DeptDO dept = deptService.getDept(user.getDeptId());
        if (dept == null) {
            return success(Collections.emptyList());
        }
        if (ObjUtil.notEqual(dept.getLeaderUserId(), id)) { // 校验为负责人
            return success(Collections.emptyList());
        }
        deptIds.add(dept.getId());
        // 1.2 获取所有子部门
        List<DeptDO> childDeptList = deptService.getChildDeptList(dept.getId());
        if (CollUtil.isNotEmpty(childDeptList)) {
            deptIds.addAll(convertSet(childDeptList, DeptDO::getId));
        }

        // 2. 获取部门对应的用户信息
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        users.removeIf(item -> ObjUtil.equal(item.getId(), id)); // 排除自己
        return success(BeanUtils.toBean(users, AdminUserRespDTO.class));
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUserList(Collection<Long> ids) {
        List<AdminUserDO> users = userService.getUserList(ids);
        return success(BeanUtils.toBean(users, AdminUserRespDTO.class));
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUserListByDeptIds(Collection<Long> deptIds) {
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        return success(BeanUtils.toBean(users, AdminUserRespDTO.class));
    }

    @Override
    public CommonResult<List<AdminUserRespDTO>> getUserListByPostIds(Collection<Long> postIds) {
        List<AdminUserDO> users = userService.getUserListByPostIds(postIds);
        return success(BeanUtils.toBean(users, AdminUserRespDTO.class));
    }

    @Override
    public CommonResult<Boolean> validateUserList(Collection<Long> ids) {
        userService.validateUserList(ids);
        return success(true);
    }

    @Override
    public CommonResult listByRole(String id) {
        List<Long> roleIds = Arrays.asList(id.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
        return permissionApi.getUserRoleIdListByRoleIds(roleIds);
    }
    @Override
    public CommonResult<List<AdminUserRespDTO>> listUserByRole(String id) {
        List<Long> roleIds = Arrays.asList(id.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
        CommonResult<Set<Long>> userRoleIdListByRoleIds = permissionApi.getUserRoleIdListByRoleIds(roleIds);
        Set<Long> data = userRoleIdListByRoleIds.getData();
        if (!data.isEmpty()){
            List<AdminUserDO> userList = userService.getUserList(data);
            return success(BeanUtils.toBean(userList, AdminUserRespDTO.class));
        }
        return CommonResult.success(new ArrayList<>());
    }

    @Override
    public CommonResult listByRoleAndDept(Collection<Long> deptIds, Collection<Long> roleIds) {
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        Set<Long> data = permissionApi.getUserRoleIdListByRoleIds(roleIds).getData();
        List<AdminUserDO> roleAndDeptUser = users.stream().filter(e -> data.contains(e.getId())).collect(Collectors.toList());
        return CommonResult.success(BeanUtils.toBean(roleAndDeptUser, AdminUserRespDTO.class));
    }


}
