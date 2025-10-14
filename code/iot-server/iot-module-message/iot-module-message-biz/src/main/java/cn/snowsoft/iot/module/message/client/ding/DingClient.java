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

package cn.snowsoft.iot.module.message.client.ding;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.model.app.DingApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 渠道-钉钉工具类
 *
 * @author oszero
 * @version 1.0.0
 */
@Slf4j
@Component
public class DingClient {

    /**
     * 获取 AccessToken
     *
     * @param dingApp 钉钉App配置
     * @return access_token
     */
    public String getAccessToken(DingApp dingApp, SendTaskDto sendTaskDto) {

        String appKey = dingApp.getAppKey();
        String appSecret = dingApp.getAppSecret();

        @Data
        class DingAccessTokenBody {

            private Integer errcode;
            private String accessToken;
            private String errmsg;
            private Integer expiresIn;

        }

        DingAccessTokenBody dingAccessTokenBody;
        try (HttpResponse response = HttpRequest.get("https://oapi.dingtalk.com/gettoken?appkey=" + appKey + "&appsecret=" + appSecret)
                .execute()) {

            dingAccessTokenBody = JSONUtil.toBean(response.body(), DingAccessTokenBody.class);

            if (!Objects.equals(dingAccessTokenBody.getErrcode(), 0)) {
                throw new MessageException(dingAccessTokenBody.getErrmsg());
            }
        } catch (Exception e) {
            throw new MessageException(sendTaskDto, "钉钉获取 Token 接口调用失败，" + e.getMessage());
        }
        log.info("获取钉钉 accessToken 成功");
        return dingAccessTokenBody.getAccessToken();
    }

    /**
     * 发送钉钉工作通知消息
     *
     * @param accessToken 钉钉accessToken
     * @param sendTaskDto 钉钉DTO
     */
    public void sendWorkNoticeMessage(String accessToken, SendTaskDto sendTaskDto) {

        @Data
        class DingSendInfoResponseBody {

            private Integer errcode;
            private String errmsg;
            private Long taskId;
            private String requestId;
        }

        Map<String, Object> paramMap = sendTaskDto.getParamMap();
        String body = JSONUtil.toJsonStr(paramMap);

        DingSendInfoResponseBody dingSendInfoResponseBody;

        try (HttpResponse response = HttpRequest.post("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token=" + accessToken)
                .header("Content-Type", "application/json; charset=utf-8")
                .body(body)
                .execute();) {

            dingSendInfoResponseBody = JSONUtil.toBean(response.body(), DingSendInfoResponseBody.class);

            if (!Objects.equals(dingSendInfoResponseBody.getErrcode(), 0)) {
                throw new MessageException(dingSendInfoResponseBody.getErrmsg());
            }
        } catch (Exception e) {
            throw new MessageException(sendTaskDto, "钉钉发送工作通知接口调用失败，" + e.getMessage());
        }

        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "发送钉钉工作通知消息成功");
    }

    /**
     * 发送钉钉机器人消息
     *
     * @param accessToken 钉钉accessToken
     * @param sendTaskDto 钉钉DTO
     */
    public void sendRobotMessage(String accessToken, SendTaskDto sendTaskDto) {

        @Data
        class DingSendInfoResponseBody {
            private String processQueryKey;
            private List<String> invalidStaffIdList;
            private List<String> flowControlledStaffIdList;
        }
        Map<String, Object> paramMap = sendTaskDto.getParamMap();

        // 选择请求地址
        String url = "https://api.dingtalk.com" + (paramMap.containsKey("userIds") ? "/v1.0/robot/oToMessages/batchSend" : "/v1.0/robot/groupMessages/send");
        String body = JSONUtil.toJsonStr(paramMap);
        DingSendInfoResponseBody dingSendInfoResponseBody;

        try (HttpResponse response = HttpRequest.post(url)
                .header("x-acs-dingtalk-access-token", accessToken)
                .header("Content-Type", "application/json")
                .body(body).execute()) {

            dingSendInfoResponseBody = JSONUtil.toBean(response.body(), DingSendInfoResponseBody.class);

            if (!(dingSendInfoResponseBody.invalidStaffIdList.isEmpty() && dingSendInfoResponseBody.flowControlledStaffIdList.isEmpty())) {
                throw new MessageException("");
            }

        } catch (Exception e) {
            throw new MessageException(sendTaskDto, "钉钉发送钉钉机器人消息接口调用失败，" + e.getMessage());
        }

        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "发送钉钉机器人消息成功");
    }

    /**
     * 根据电话号码获取userId
     *
     * @param accessToken 钉钉accessToken
     * @param phone       电话号码
     * @return userId
     */

    public String getUserIdByPhone(String accessToken, String phone, SendTaskDto sendTaskDto) {

        @Data
        class RequestBody {
            private String mobile;
            private boolean support_exclusive_account_search;
        }

        @Data
        class Result {
            private String userid;
            private String[] exclusive_account_userid_list;
        }

        @Data
        class DingResponseBody {
            private Integer errcode;
            private String requestId;
            private Result result;
            private String errmsg;
        }

        RequestBody requestBody = new RequestBody();
        requestBody.setMobile(phone);
        requestBody.setSupport_exclusive_account_search(true);

        DingResponseBody dingResponseBody;

        try (HttpResponse response = HttpRequest.post("https://oapi.dingtalk.com/topapi/v2/user/getbymobile?access_token=" + accessToken)
                .body(JSONUtil.toJsonStr(requestBody))
                .execute()) {
            dingResponseBody = JSONUtil.toBean(response.body(), DingResponseBody.class);

            if (!Objects.equals(dingResponseBody.getErrcode(), 0)) {
                throw new MessageException(dingResponseBody.getErrmsg());
            }
        } catch (Exception e) {
            throw new MessageException(sendTaskDto, "钉钉根据电话号码获取 userId 接口调用失败，" + e.getMessage());
        }

        return dingResponseBody.getResult().getUserid();
    }
}
