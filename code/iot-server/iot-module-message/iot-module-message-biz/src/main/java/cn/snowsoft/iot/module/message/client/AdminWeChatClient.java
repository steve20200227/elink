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

package cn.snowsoft.iot.module.message.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import cn.snowsoft.iot.module.message.exception.BusinessException;
import cn.snowsoft.iot.module.message.model.app.WeChatApp;
import cn.snowsoft.iot.module.message.model.dto.app.PlatformFileDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * App-企业微信工具类
 *
 * @author oszero
 * @version 1.0.0
 */
@Slf4j
@Component
public class AdminWeChatClient {

    /**
     * 获取企业微信 AccessToken
     *
     * @param weChatApp 企微 APP
     * @return AccessToken
     */
    public String getAccessToken(WeChatApp weChatApp) {

        String corpid = weChatApp.getCorpid();
        String corpsecret = weChatApp.getCorpsecret();
        @Data
        class WechatResponse {
            private Integer errcode;
            private String errmsg;
            private String access_token;
            private Integer expires_in;
        }

        WechatResponse weChatResponse;
        try (HttpResponse response = HttpRequest.get("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret)
                .execute()) {

            weChatResponse = JSONUtil.toBean(response.body(), WechatResponse.class);
            if (!Objects.equals(weChatResponse.getErrcode(), 0)) {
                throw new BusinessException(weChatResponse.getErrmsg());
            }
        } catch (Exception e) {
            throw new BusinessException("获取企业微信 AccessToken 失败，" + e.getMessage() + "！！！");
        }
        log.info("获取企微 AccessToken 成功");
        return weChatResponse.getAccess_token();
    }

    /**
     * 上传企微文件
     *
     * @param accessToken     token
     * @param platformFileDto 文件 dto
     * @return media_id
     */
    public String uploadWeChatFile(String accessToken, PlatformFileDto platformFileDto) {
        String fileType = platformFileDto.getFileType();
        @Data
        class WechatResponse {
            private Integer errcode;
            private String errmsg;
            private String type;
            private String media_id;
            private String created_at;
        }

        try (HttpResponse response = HttpRequest.post("https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=" + fileType)
                .header("Content-Type", "multipart/form-data; boundary=-------------------------acebdf13572468")
                .form("media", platformFileDto.getFile(), platformFileDto.getFileName())
                .execute()) {
            WechatResponse wechatResponse = JSONUtil.toBean(response.body(), WechatResponse.class);
            if (!Objects.equals(wechatResponse.getErrcode(), 0)) {
                throw new BusinessException(wechatResponse.getErrmsg());
            }
            log.info("上传企微平台文件成功");
            return wechatResponse.getMedia_id();
        } catch (Exception e) {
            throw new BusinessException("上传企业微信平台文件失败，" + e.getMessage() + "！！！");
        }
    }
}
