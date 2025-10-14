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

package cn.snowsoft.iot.module.message.cache.manager;

import cn.snowsoft.iot.module.message.client.ding.DingClient;
import cn.snowsoft.iot.module.message.client.feishu.FeiShuClient;
import cn.snowsoft.iot.module.message.client.wechat.WeChatClient;
import cn.snowsoft.iot.module.message.model.app.DingApp;
import cn.snowsoft.iot.module.message.model.app.FeiShuApp;
import cn.snowsoft.iot.module.message.model.app.WeChatApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.model.entity.App;
import cn.snowsoft.iot.module.message.model.entity.Template;
import cn.snowsoft.iot.module.message.model.entity.TemplateApp;
import cn.snowsoft.iot.module.message.service.AppService;
import cn.snowsoft.iot.module.message.service.TemplateAppService;
import cn.snowsoft.iot.module.message.service.TemplateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

/**
 * 服务端各缓存管理器
 *
 * @author oszero
 * @version 1.0.0
 */
@Service
@ConditionalOnBean(name = "cacheEnableProperty")
@RequiredArgsConstructor
public class ServerCacheManager {


    private final TemplateService templateService;
    private final TemplateAppService templateAppService;
    private final AppService appService;

    private final DingClient dingClient;
    private final WeChatClient weChatClient;
    private final FeiShuClient feiShuClient;

    /**
     * 缓存建立
     *
     * @param templateId 模板ID
     * @return Template
     */
//    @Cacheable(value = CacheConstant.TEMPLATE_CACHE_NAME, key = "#templateId",
//            cacheManager = CacheConstant.REDIS_CACHE_MANAGER)
    public Template getTemplate(Long templateId) {
        return templateService.getById(templateId);
    }

    /**
     * 缓存建立
     *
     * @param templateId 模板ID
     * @return TemplateApp
     */
//    @Cacheable(value = CacheConstant.TEMPLATE_APP_CACHE_NAME, key = "#templateId",
//            cacheManager = CacheConstant.REDIS_CACHE_MANAGER)
    public TemplateApp getTemplateApp(Long templateId) {
        LambdaQueryWrapper<TemplateApp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TemplateApp::getTemplateId, templateId);
        return templateAppService.getOne(wrapper);
    }

    /**
     * 缓存建立
     *
     * @param appId 应用ID
     * @return app
     */
//    @Cacheable(value = CacheConstant.APP_CACHE_NAME, key = "#appId",
//            cacheManager = CacheConstant.REDIS_CACHE_MANAGER)
    public App getApp(Long appId) {
        return appService.getById(appId);
    }

    /**
     * 缓存建立
     *
     * @param dingApp 应用
     * @return Token
     */
    //    @Cacheable(value = CacheConstant.CLIENT_TOKEN_CACHE_NAME, keyGenerator = "tokenKeyGenerator",
//            cacheManager = CacheConstant.REDIS_CACHE_MANAGER)
    public String getDingToken(DingApp dingApp, SendTaskDto sendTaskDto) {
        return dingClient.getAccessToken(dingApp, sendTaskDto);
    }

    /**
     * 缓存建立
     *
     * @param weChatApp 应用
     * @return Token
     */
    //    @Cacheable(value = CacheConstant.CLIENT_TOKEN_CACHE_NAME, keyGenerator = "tokenKeyGenerator",
//            cacheManager = CacheConstant.REDIS_CACHE_MANAGER)
    public String getWeChatToken(WeChatApp weChatApp, SendTaskDto sendTaskDto) {
        return weChatClient.getAccessToken(weChatApp, sendTaskDto);
    }

    /**
     * 缓存建立
     *
     * @param feiShuApp 应用
     * @return Token
     */
    //    @Cacheable(value = CacheConstant.CLIENT_TOKEN_CACHE_NAME, keyGenerator = "tokenKeyGenerator",
//            cacheManager = CacheConstant.REDIS_CACHE_MANAGER)
    public String getFeiShuToken(FeiShuApp feiShuApp, SendTaskDto sendTaskDto) {
        return feiShuClient.getTenantAccessToken(feiShuApp, sendTaskDto);
    }
}
