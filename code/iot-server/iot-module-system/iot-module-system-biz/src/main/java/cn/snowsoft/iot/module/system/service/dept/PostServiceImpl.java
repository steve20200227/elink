package cn.snowsoft.iot.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.snowsoft.iot.framework.common.enums.CommonStatusEnum;
import cn.snowsoft.iot.framework.common.pojo.PageResult;
import cn.snowsoft.iot.framework.common.util.object.BeanUtils;
import cn.snowsoft.iot.module.system.controller.admin.dept.vo.post.PostPageReqVO;
import cn.snowsoft.iot.module.system.controller.admin.dept.vo.post.PostSaveReqVO;
import cn.snowsoft.iot.module.system.dal.dataobject.dept.PostDO;
import cn.snowsoft.iot.module.system.dal.dataobject.dept.UserPostDO;
import cn.snowsoft.iot.module.system.dal.dataobject.user.AdminUserDO;
import cn.snowsoft.iot.module.system.dal.mysql.dept.PostMapper;
import cn.snowsoft.iot.module.system.dal.mysql.dept.UserPostMapper;
import cn.snowsoft.iot.module.system.dal.mysql.user.AdminUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.snowsoft.iot.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.snowsoft.iot.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.snowsoft.iot.module.system.enums.ErrorCodeConstants.*;

/**
 * 岗位 Service 实现类
 *
 * @author chengang
 */
@Service
@Validated
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private UserPostMapper userPostMapper;
    @Resource
    private AdminUserMapper userMapper;

    @Override
    public Long createPost(PostSaveReqVO createReqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(null, createReqVO.getName(), createReqVO.getCode());

        // 插入岗位
        PostDO post = BeanUtils.toBean(createReqVO, PostDO.class);
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    public void updatePost(PostSaveReqVO updateReqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(updateReqVO.getId(), updateReqVO.getName(), updateReqVO.getCode());

        // 更新岗位
        PostDO updateObj = BeanUtils.toBean(updateReqVO, PostDO.class);
        postMapper.updateById(updateObj);
        //如果关闭 删除关联表数据
        System.out.println(CommonStatusEnum.DISABLE.getStatus().toString());
        System.out.println(updateReqVO.getStatus());
        if (CommonStatusEnum.DISABLE.getStatus().equals(updateReqVO.getStatus())) {
            List list = new ArrayList();
            list.add(updateReqVO.getId());
            //查询所有关联的用户
            List<UserPostDO> userPostDOList = userPostMapper.selectListByPostIds(list);

            List<Long> userIdList = userPostDOList.stream().map(UserPostDO::getUserId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(userIdList)){
                LambdaQueryWrapper<AdminUserDO> adminUserQueryWrapper = new LambdaQueryWrapper<>();
                adminUserQueryWrapper.in(AdminUserDO::getId, userIdList);
                List<AdminUserDO> adminUserDOS = userMapper.selectList(adminUserQueryWrapper);

                //修改每个用户的 postIds
                for (AdminUserDO adminUserDO : adminUserDOS) {
                    Set<Long> postIds = adminUserDO.getPostIds();
                    postIds.remove(updateReqVO.getId());
                    adminUserDO.setPostIds(postIds);
                    userMapper.updateById(adminUserDO);
                }
                LambdaQueryWrapper<UserPostDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(UserPostDO::getPostId, updateReqVO.getId());
                userPostMapper.delete(lambdaQueryWrapper);
            }

        }
    }

    @Override
    public void deletePost(Long id) {
        // 校验是否存在
        validatePostExists(id);
        // 删除部门
        postMapper.deleteById(id);
    }

    private void validatePostForCreateOrUpdate(Long id, String name, String code) {
        // 校验自己存在
        validatePostExists(id);
        // 校验岗位名的唯一性
        validatePostNameUnique(id, name);
        // 校验岗位编码的唯一性
        validatePostCodeUnique(id, code);
    }

    private void validatePostNameUnique(Long id, String name) {
        PostDO post = postMapper.selectByName(name);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(POST_NAME_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw exception(POST_NAME_DUPLICATE);
        }
    }

    private void validatePostCodeUnique(Long id, String code) {
        PostDO post = postMapper.selectByCode(code);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(POST_CODE_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw exception(POST_CODE_DUPLICATE);
        }
    }

    private void validatePostExists(Long id) {
        if (id == null) {
            return;
        }
        if (postMapper.selectById(id) == null) {
            throw exception(POST_NOT_FOUND);
        }
    }

    @Override
    public List<PostDO> getPostList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return postMapper.selectBatchIds(ids);
    }

    @Override
    public List<PostDO> getPostList(Collection<Long> ids, Collection<Integer> statuses) {
        return postMapper.selectList(ids, statuses);
    }

    @Override
    public PageResult<PostDO> getPostPage(PostPageReqVO reqVO) {
        return postMapper.selectPage(reqVO);
    }

    @Override
    public PostDO getPost(Long id) {
        return postMapper.selectById(id);
    }

    @Override
    public void validatePostList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<PostDO> posts = postMapper.selectBatchIds(ids);
        Map<Long, PostDO> postMap = convertMap(posts, PostDO::getId);
        // 校验
        ids.forEach(id -> {
            PostDO post = postMap.get(id);
            if (post == null) {
                throw exception(POST_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(post.getStatus())) {
                throw exception(POST_NOT_ENABLE, post.getName());
            }
        });
    }
}
