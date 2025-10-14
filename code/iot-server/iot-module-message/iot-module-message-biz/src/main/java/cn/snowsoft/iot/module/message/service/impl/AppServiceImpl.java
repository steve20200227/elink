/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.snowsoft.iot.module.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.message.cache.AdminServerCacheManager;
import cn.snowsoft.iot.module.message.constant.AppConfigConstant;
import cn.snowsoft.iot.module.message.exception.BusinessException;
import cn.snowsoft.iot.module.message.mapper.AppMapper;
import cn.snowsoft.iot.module.message.model.dto.request.*;
import cn.snowsoft.iot.module.message.model.dto.response.AppByChannelResponseDto;
import cn.snowsoft.iot.module.message.model.dto.response.AppSearchResponseDto;
import cn.snowsoft.iot.module.message.model.entity.App;
import cn.snowsoft.iot.module.message.model.entity.TemplateApp;
import cn.snowsoft.iot.module.message.service.AppService;
import cn.snowsoft.iot.module.message.service.TemplateAppService;
import cn.snowsoft.iot.module.message.util.AdminAesUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * app serviceImpl
 *
 * @author oszero
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AppServiceImpl extends ServiceImpl<AppMapper, App>
        implements AppService {

    private final TemplateAppService templateAppService;
    private final AdminAesUtils adminAesUtils;
    private final AppMapper appMapper;
    private final AdminServerCacheManager adminServerCacheManager;

    @Override
    public Page<AppSearchResponseDto> getAppPagesByCondition(AppSearchRequestDto dto) {
        LambdaQueryWrapper<App> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(dto.getAppName()), App::getAppName, dto.getAppName())
                .eq(!Objects.isNull(dto.getChannelType()), App::getChannelType, dto.getChannelType())
                .gt(!Objects.isNull(dto.getStartTime()), App::getCreateTime, dto.getStartTime())
                .lt(!Objects.isNull(dto.getStartTime()), App::getCreateTime, dto.getEndTime())
                .orderByDesc(App::getCreateTime);

        Page<App> appPage = new Page<>(dto.getCurrentPage(), dto.getPageSize());
        this.page(appPage, wrapper);
        List<AppSearchResponseDto> collect = appPage.getRecords().stream()
                .map(app -> {
                    AppSearchResponseDto appSearchResponseDto = new AppSearchResponseDto();
                    BeanUtil.copyProperties(app, appSearchResponseDto);
                    appSearchResponseDto.setAppConfig(adminAesUtils.decrypt(app.getAppConfig()));
                    return appSearchResponseDto;
                }).collect(Collectors.toList());

        Page<AppSearchResponseDto> page = new Page<>(appPage.getPages(), appPage.getSize());
        page.setRecords(collect);
        page.setTotal(appPage.getTotal());
        return page;
    }

    @Override
    public CommonResult deleteByIds(DeleteIdsRequestDto dto) {
        ArrayList<Long> errRes = new ArrayList<>();
        dto.getIds().forEach(appId -> {
            LambdaQueryWrapper<TemplateApp> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TemplateApp::getAppId, appId);
            List<TemplateApp> list = templateAppService.list(wrapper);
            if (!CollUtil.isEmpty(list)) {
                errRes.add(appId);
            }
        });
        if (!CollUtil.isEmpty(errRes)) {
            return CommonResult.error(207, "以下通道已关联模板，请解除关联关系后再删除，关联模板 ID 为：" + errRes);
        }
        int deleted = appMapper.deleteBatchIds(dto.getIds());
        if (!Objects.equals(dto.getIds().size(), deleted)) {
            return CommonResult.error(207, "删除通道失败");
        }
        // 删除缓存
        dto.getIds().forEach(adminServerCacheManager::evictApp);
        return CommonResult.success("删除成功");
    }

    @Override
    public void updateById(AppSaveAndUpdateRequestDto dto) {
        // 去掉两边空格
        dto.setAppName(dto.getAppName().trim());
        // 去重判断
        checkAppNameIsDuplicate(dto);

        App app = new App();
        BeanUtil.copyProperties(dto, app);
        app.setAppConfig(adminAesUtils.encrypt(dto.getAppConfig()));
        boolean update = this.updateById(app);
        if (!update) {
            throw new BusinessException("app 更新失败！！！");
        }
        // 删除缓存
        adminServerCacheManager.evictApp(dto.getAppId());
    }

    @Override
    public void save(AppSaveAndUpdateRequestDto dto) {
        // 去掉两边空格
        dto.setAppName(dto.getAppName().trim());
        // 去重判断
        checkAppNameIsDuplicate(dto);

        App app = new App();
        BeanUtil.copyProperties(dto, app);
        app.setAppConfig(adminAesUtils.encrypt(dto.getAppConfig()));
        boolean save = this.save(app);
        if (!save) {
            throw new BusinessException("app 保存失败！！！");
        }
    }

    @Override
    public void updateStatusById(AppUpdateStatusRequestDto dto) {
        App app = new App();
        BeanUtil.copyProperties(dto, app);
        boolean update = this.updateById(app);
        if (!update) {
            throw new BusinessException("app 状态更新失败！！！");
        }
        // 删除缓存
        adminServerCacheManager.evictApp(dto.getAppId());
    }

    @Override
    public List<AppByChannelResponseDto> getAppByChannelType(TemplateAddGetByChannelRequestDto dto) {
        Integer channelType = dto.getChannelType();
        LambdaQueryWrapper<App> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(App::getChannelType, channelType)
                .orderByDesc(App::getCreateTime);
        List<App> list = this.list(wrapper);
        return list.stream().map(app -> {
            AppByChannelResponseDto appByChannelResponseDto = new AppByChannelResponseDto();
            BeanUtil.copyProperties(app, appByChannelResponseDto);
            return appByChannelResponseDto;
        }).collect(Collectors.toList());
    }

    private void checkAppNameIsDuplicate(AppSaveAndUpdateRequestDto dto) {
        String appName = dto.getAppName();
        Long appId = dto.getAppId();

        LambdaQueryWrapper<App> wrapper = new LambdaQueryWrapper<>();

        // 为 null 表示新增
        if (Objects.isNull(appId)) {
            wrapper.eq(App::getAppName, appName);
            App one = this.getOne(wrapper);
            if (!Objects.isNull(one)) {
                throw new BusinessException("此 APP 名(" + appName + ")已存在！！！");
            }
        } else {
            wrapper.eq(App::getAppName, appName)
                    .or().eq(App::getAppId, appId);
            List<App> list = this.list(wrapper);
            if (list.size() > 1) {
                throw new BusinessException("此 APP 名(" + appName + ")已存在！！！");
            }
        }
    }

    @Override
    public String getAppConfigByChannelType(AppConfigByChannelRequestDto dto) {
        Integer channelType = dto.getChannelType();

        switch (channelType) {
            case 1:
                return AppConfigConstant.CALL_CONFIG;
            case 2:
                return AppConfigConstant.SMS_CONFIG;
            case 3:
                return AppConfigConstant.MAIL_CONFIG;
            case 4:
                return AppConfigConstant.DING_CONFIG;
            case 5:
                return AppConfigConstant.WECHAT_CONFIG;
            case 6:
                return AppConfigConstant.FEI_SHU_CONFIG;
            default:
                return "{}";
        }
    }
}




