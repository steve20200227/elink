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
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.snowsoft.iot.module.message.client.AdminDingClient;
import cn.snowsoft.iot.module.message.client.AdminFeiShuClient;
import cn.snowsoft.iot.module.message.client.AdminWeChatClient;
import cn.snowsoft.iot.module.message.constant.PlatformFileConstant;
import cn.snowsoft.iot.module.message.enums.AppTypeEnum;
import cn.snowsoft.iot.module.message.enums.PlatformFileTypeEnum;
import cn.snowsoft.iot.module.message.exception.BusinessException;
import cn.snowsoft.iot.module.message.mapper.PlatformFileMapper;
import cn.snowsoft.iot.module.message.model.app.DingApp;
import cn.snowsoft.iot.module.message.model.app.FeiShuApp;
import cn.snowsoft.iot.module.message.model.app.WeChatApp;
import cn.snowsoft.iot.module.message.model.dto.app.PlatformFileDto;
import cn.snowsoft.iot.module.message.model.dto.request.PlatformFileSearchRequestDto;
import cn.snowsoft.iot.module.message.model.dto.request.PlatformFileUploadRequestDto;
import cn.snowsoft.iot.module.message.model.dto.response.PlatformFileSearchResponseDto;
import cn.snowsoft.iot.module.message.model.entity.App;
import cn.snowsoft.iot.module.message.model.entity.PlatformFile;
import cn.snowsoft.iot.module.message.service.AppService;
import cn.snowsoft.iot.module.message.service.PlatformFileService;
import cn.snowsoft.iot.module.message.util.AdminAesUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 针对表【platform_file(平台文件表)】的数据库操作Service实现
 *
 * @author oszero
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class PlatformFileServiceImpl extends ServiceImpl<PlatformFileMapper, PlatformFile>
        implements PlatformFileService {

    private final AdminDingClient adminDingClient;
    private final AdminFeiShuClient adminFeiShuClient;
    private final AdminWeChatClient adminWeChatClient;
    private final AppService appService;
    private final AdminAesUtils adminAesUtils;

    @Override
    public void uploadFile(PlatformFileUploadRequestDto dto) throws IOException {
        // 获取文件
        MultipartFile platformFile = dto.getPlatformFile();

        // 获取 APP 类型枚举
        Integer appType = dto.getAppType();
        AppTypeEnum appTypeEnum = AppTypeEnum.getInstanceByCode(appType);
        if (Objects.isNull(appTypeEnum)) {
            throw new BusinessException("appType 错误！！！");
        }

        // 获取 APP 实体
        Long appId = dto.getAppId();
        App app = appService.getById(appId);
        if (Objects.isNull(app)) {
            throw new BusinessException("appId 不存在！！！");
        }

        // 获取相关文件信息
        String originalFilename = platformFile.getOriginalFilename();
        long fileSize = platformFile.getSize();
        if (StrUtil.isBlank(originalFilename)) {
            throw new BusinessException("上传文件错误！！！");
        }
        String fileFormat = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        // 设置发送具体平台 DTO
        PlatformFileDto platformFileDto = new PlatformFileDto();
        platformFileDto.setFile(platformFile.getBytes());
        platformFileDto.setFileName(dto.getFileName() + "." + fileFormat);
        platformFileDto.setFileType(dto.getFileType());

        // 设置 PlatformFile 实体
        PlatformFile platformFileEntity = new PlatformFile();
        platformFileEntity.setFileName(dto.getFileName());
        platformFileEntity.setFileType(dto.getAppType() + "-" + dto.getFileType());
        platformFileEntity.setAppType(dto.getAppType());
        platformFileEntity.setAppId(dto.getAppId());
        String fileKey = "";

        // 得到 APP 配置
        String appConfig = adminAesUtils.decrypt(app.getAppConfig());
        // 具体执行选择
        switch (appTypeEnum) {
            case DING : {

                DingApp dingApp = JSONUtil.toBean(appConfig, DingApp.class);
                String accessToken = adminDingClient.getAccessToken(dingApp);

                if (PlatformFileTypeEnum.DING_VOICE.getFileType().equals(dto.getFileType())) {
                    if (!PlatformFileConstant.DING_FILE_FORMAT_SET.contains(fileFormat.toLowerCase(Locale.ROOT))) {
                        throw new BusinessException("不支持 " + fileFormat + " 格式的语音！！！");
                    }
                    if (fileSize > PlatformFileConstant.DING_VOICE_MAX_SIZE) {
                        throw new BusinessException("语音最大为：2M！！！");
                    }
                } else {
                    if (!PlatformFileConstant.DING_FILE_FORMAT_SET.contains(fileFormat.toLowerCase(Locale.ROOT))) {
                        throw new BusinessException("不支持 " + fileFormat + " 格式的文件！！！");
                    }
                    if (fileSize > PlatformFileConstant.DING_FILE_MAX_SIZE) {
                        throw new BusinessException("文件最大为：20M！！！");
                    }
                }
                fileKey = adminDingClient.uploadDingFile(accessToken, platformFileDto);
            }
            break;
            case WECHAT : {
                // 得到 token
                WeChatApp weChatApp = JSONUtil.toBean(appConfig, WeChatApp.class);
                String accessToken = adminWeChatClient.getAccessToken(weChatApp);

                // 发送
                if (PlatformFileTypeEnum.WECHAT_IMAGE.getFileType().equals(dto.getFileType())) {
                    if (!PlatformFileConstant.WECHAT_IMAGE_FORMAT_SET.contains(fileFormat.toLowerCase(Locale.ROOT))) {
                        throw new BusinessException("不支持 " + fileFormat + " 格式的图片！！！");
                    }
                    if (fileSize > PlatformFileConstant.WECHAT_IMAGE_MAX_SIZE) {
                        throw new BusinessException("图片最大为：10M！！！");
                    }
                    fileKey = adminWeChatClient.uploadWeChatFile(accessToken, platformFileDto);
                } else if (PlatformFileTypeEnum.WECHAT_VOICE.getFileType().equals(dto.getFileType())) {
                    if (!PlatformFileConstant.WECHAT_VOICE_FORMAT_SET.contains(fileFormat.toLowerCase(Locale.ROOT))) {
                        throw new BusinessException("不支持 " + fileFormat + " 格式的音频！！！");
                    }
                    if (fileSize > PlatformFileConstant.WECHAT_VOICE_MAX_SIZE) {
                        throw new BusinessException("音频最大为：2M！！！");
                    }
                    fileKey = adminWeChatClient.uploadWeChatFile(accessToken, platformFileDto);
                } else if (PlatformFileTypeEnum.WECHAT_VIDEO.getFileType().equals(dto.getFileType())) {
                    if (!PlatformFileConstant.WECHAT_VIDEO_FORMAT_SET.contains(fileFormat.toLowerCase(Locale.ROOT))) {
                        throw new BusinessException("不支持 " + fileFormat + " 格式的视频！！！");
                    }
                    if (fileSize > PlatformFileConstant.WECHAT_VIDEO_MAX_SIZE) {
                        throw new BusinessException("视频最大为：10M！！！");
                    }
                    fileKey = adminWeChatClient.uploadWeChatFile(accessToken, platformFileDto);
                } else {
                    if (fileSize > PlatformFileConstant.WECHAT_FILE_MAX_SIZE) {
                        throw new BusinessException("文件最大为：20M！！！");
                    }
                    fileKey = adminWeChatClient.uploadWeChatFile(accessToken, platformFileDto);
                }
            }
            break;
            case FEI_SHU : {
                FeiShuApp feiShuApp = JSONUtil.toBean(appConfig, FeiShuApp.class);
                // 得到 token
                String tenantAccessToken = adminFeiShuClient.getTenantAccessToken(feiShuApp);
                // 发送
                if (PlatformFileTypeEnum.FEI_SHU_IMAGE.getFileType().equals(dto.getFileType())) {
                    if (!PlatformFileConstant.FEI_SHU_IMAGE_FORMAT_SET.contains(fileFormat.toLowerCase(Locale.ROOT))) {
                        throw new BusinessException("不支持 " + fileFormat + " 格式的图片！！！");
                    }
                    if (fileSize > PlatformFileConstant.FEI_SHU_IMAGE_FILE_MAX_SIZE) {
                        throw new BusinessException("图片最大为：10M！！！");
                    }
                    fileKey = adminFeiShuClient.uploadFeiShuImageFile(tenantAccessToken, platformFileDto);
                } else {
                    if (!PlatformFileConstant.FEI_SHU_FILE_FORMAT_SET.contains(fileFormat.toLowerCase(Locale.ROOT))) {
                        throw new BusinessException("不支持 " + fileFormat + " 格式的文件！！！");
                    }
                    if (fileSize > PlatformFileConstant.FEI_SHU_FILE_MAX_SIZE) {
                        throw new BusinessException("文件最大为：30M！！！");
                    }
                    fileKey = adminFeiShuClient.uploadFeiShuFile(tenantAccessToken, platformFileDto);
                }
            }
            break;
            default : {
            }
        }

        // 保存入库
        platformFileEntity.setFileKey(fileKey);
        this.save(platformFileEntity);
    }

    @Override
    public Page<PlatformFileSearchResponseDto> getPagePlatformFile(PlatformFileSearchRequestDto dto) {
        LambdaQueryWrapper<PlatformFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(dto.getFileName()), PlatformFile::getFileName, dto.getFileName())
                .eq(!Objects.isNull(dto.getAppType()), PlatformFile::getAppType, dto.getAppType())
                .like(StrUtil.isNotBlank(dto.getFileType()), PlatformFile::getFileType, dto.getFileType())
                .like(StrUtil.isNotBlank(dto.getFileKey()), PlatformFile::getFileKey, dto.getFileKey())
                .eq(!Objects.isNull(dto.getAppId()), PlatformFile::getAppId, dto.getAppId())
                .ge(!Objects.isNull(dto.getStartTime()), PlatformFile::getCreateTime, dto.getStartTime())
                .le(!Objects.isNull(dto.getEndTime()), PlatformFile::getCreateTime, dto.getStartTime())
                .orderByDesc(PlatformFile::getCreateTime);
        Page<PlatformFile> platformFilePage = new Page<>(dto.getCurrentPage(), dto.getPageSize());

        this.page(platformFilePage, wrapper);

        LocalDateTime minusDays = LocalDateTime.now().minusDays(3L);
        Page<PlatformFileSearchResponseDto> platformFileSearchResponseDtoPage = new Page<>(platformFilePage.getPages(), platformFilePage.getSize());
        platformFileSearchResponseDtoPage.setRecords(platformFilePage.getRecords().stream().map(platformFile -> {
            PlatformFileSearchResponseDto platformFileSearchResponseDto = new PlatformFileSearchResponseDto();
            BeanUtil.copyProperties(platformFile, platformFileSearchResponseDto);
            // 企微三天有效期
            if (Objects.equals(AppTypeEnum.WECHAT.getCode(), platformFile.getAppType())) {
                platformFileSearchResponseDto.setFileStatus(
                        // 创建时间为3天前即失效
                        platformFile.getCreateTime().isBefore(minusDays) ? 0 : 1
                );
            }
            platformFileSearchResponseDto.setFileType(PlatformFileConstant.FILE_TYPE_NAME_MAP.get(platformFile.getFileType()));
            return platformFileSearchResponseDto;
        }).collect(Collectors.toList()));

        platformFileSearchResponseDtoPage.setTotal(platformFilePage.getTotal());

        return platformFileSearchResponseDtoPage;
    }
}




