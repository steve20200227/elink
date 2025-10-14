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

package cn.snowsoft.iot.module.message.constant;



import cn.snowsoft.iot.module.message.enums.ChannelTypeEnum;
import cn.snowsoft.iot.module.message.enums.MessageTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息参数常量
 *
 * @author oszero
 * @version 1.0.0
 */
public class MessageParamConstant {
    public static final Map<String, String> MESSAGE_PARAM_MAP = new HashMap<>();
    public static final String H = "-";

    static {
        // 电话消息参数
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.CALL.getCode() + H + MessageTypeEnum.TEXT.getCode(), "{\n" +
                "                   \"aliyun\": {\n" +
                "                     \"callProvider\": \"aliyun --指定提供商 必须\",\n" +
                "                     \"region\": \"服务地址\",\n" +
                "                     \"calledShowNumber\": \"被叫显号  非必须\",\n" +
                "                     \"ttsCode\": \"已通过审核的语音通知文本转语音模板或语音验证码模板的模板 ID 必须\",\n" +
                "                     \"ttsParam\": \"模板中的变量参数   非必须\",\n" +
                "                     \"playTimes\": \"一通电话内语音通知内容的播放次数 非必须\",\n" +
                "                     \"volume\": \"语音通知的播放音量  非必须\",\n" +
                "                     \"speed\": \"语速控制 非必须\",\n" +
                "                     \"outId\": \"发起请求时预留给调用方的自定义 ID，最终会通过在回执消息中将此 ID 带回给调用方。非必须\"\n" +
                "                   },\n" +
                "                   \"tencent\": {\n" +
                "                     \"callProvider\": \"tencent --指定提供商 必填\",\n" +
                "                     \"region\": \"地域列表 ap-beijing 或者 ap-guangzhou 必填\",\n" +
                "                     \"templateId\": \"模板 ID，在控制台审核通过的模板 ID。 必填\",\n" +
                "                     \"templateParamSet\": [\n" +
                "                       \"1 模板参数，若模板没有参数，请提供为空数组。 非必填\"\n" +
                "                     ],\n" +
                "                     \"playTimes\": 1,\n" +
                "                     \"sessionContext\": \"1 用户的 session 内容，腾讯 server 回包中会原样返回。 非必填\",\n" +
                "                     \"voiceSdkAppid\": \"1 在语音控制台添加应用后生成的实际SdkAppid 非必填\"\n" +
                "                   }\n" +
                "                 }");
        // 短信消息参数
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.SMS.getCode() + H + MessageTypeEnum.TEXT.getCode(), "{\n" +
                "                   \"aliyun\": {\n" +
                "                     \"smsProvider\": \"aliyun --指定提供商 必须\",\n" +
                "                     \"region\": \"服务地址 cn-zhangjiakou 或者 cn-beijing 或者 cn-huhehaote 等 必须\",\n" +
                "                     \"signName\": \"短信签名名称  必须\",\n" +
                "                     \"templateCode\": \"短信模板Code 必须\",\n" +
                "                     \"templateParam\": \"{有参数情况下必须}\",\n" +
                "                     \"outId\": \"外部流水扩展字段   非必须\",\n" +
                "                     \"smsUpExtendCode\": \"上行短信扩展码 非必须\"\n" +
                "                   },\n" +
                "                   \"tencent\": {\n" +
                "                     \"smsProvider\": \"tencent 必填\",\n" +
                "                     \"region\": \"ap-beijing 或 ap-guangzhou 或 ap-nanjing 必填\",\n" +
                "                     \"smsSdkAppId\": \"必填\",\n" +
                "                     \"signName\": \"必填\",\n" +
                "                     \"templateId\": \"必填\",\n" +
                "                     \"templateParam\": [\n" +
                "                       \"有动态参数，必填\"\n" +
                "                     ],\n" +
                "                     \"extendCode\": \"短信码号扩展号 非必填\",\n" +
                "                     \"sessionContext\": \"用户的 session 内容，可以携带用户侧 ID 等上下文信息，server 会原样返回 非必填\",\n" +
                "                     \"senderId\": \"国内短信无需填写该项；国际/港澳台短信已申请独立 SenderId 需要填写该字段，默认使用公共 SenderId，无需填写该字段。 非必填\"\n" +
                "                   }\n" +
                "                 }");
        // 邮件消息参数
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.MAIL.getCode() + H + MessageTypeEnum.TEXT.getCode(), "{\n" +
                "                    \"title\": \"dsadas\",\n" +
                "                    \"content\": \"test\",\n" +
                "                    \"toCC\": [\n" +
                "                        \"xxx\"\n" +
                "                    ],\n" +
                "                    \"toBCC\": [\n" +
                "                        \"xxx\"\n" +
                "                    ],\n" +
                "                    \"htmlFlag\": true\n" +
                "                }");

        // 钉钉消息参数
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.DING.getCode() + H + MessageTypeEnum.TEXT.getCode(), "{\n" +
                "                  \"pushSubject\": \"workNotice 或者 robot\",\n" +
                "                  \"dingUserIdType\": \"userid_list 或者 dept_id_list 或者 userIds 或者 openConversationId\",\n" +
                "                  \"msgParam\": {\n" +
                "                    \"content\": \"xxxx\"\n" +
                "                  },\n" +
                "                  \"msg\": {\n" +
                "                    \"text\": {\n" +
                "                      \"content\": \"月会通知\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.DING.getCode() + H + MessageTypeEnum.DING_IMAGE.getCode(), "{\n" +
                "                  \"dingUserIdType\": \"userid_list 或者 dept_id_list 或者 userIds 或者 openConversationId\",\n" +
                "                  \"pushSubject\": \"workNotice 或者 robot\",\n" +
                "                  \"msgParam\": {\n" +
                "                    \"photoURL\": \"xxxx\"\n" +
                "                  },\n" +
                "                  \"msg\": {\n" +
                "                    \"image\": {\n" +
                "                      \"media_id\": \"\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.DING.getCode() + H + MessageTypeEnum.DING_VOICE.getCode(), " {\n" +
                "                  \"dingUserIdType\": \"userid_list 或者 dept_id_list 或者 userIds 或者 openConversationId\",\n" +
                "                  \"pushSubject\": \"workNotice 或者 robot\",\n" +
                "                  \"msgParam\": {\n" +
                "                    \"mediaId\": \"@IR_P********nFkfhsisbf4A\",\n" +
                "                    \"duration\": \"xxxxx\"\n" +
                "                  },\n" +
                "                  \"msg\": {\n" +
                "                    \"voice\": {\n" +
                "                      \"media_id\": \"\",\n" +
                "                      \"duration\": \"10\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.DING.getCode() + H + MessageTypeEnum.DING_FILE.getCode(), "{\n" +
                "                  \"dingUserIdType\": \"userid_list 或者 dept_id_list 或者 userIds 或者 openConversationId\",\n" +
                "                  \"pushSubject\": \"workNotice 或者 robot\",\n" +
                "                  \"msgParam\": {\n" +
                "                    \"mediaId\": \"@lAz*********shRs5m2pRL\",\n" +
                "                    \"fileName\": \"表格.xlsx\",\n" +
                "                    \"fileType\": \"xlsx\"\n" +
                "                  },\n" +
                "                  \"msg\": {\n" +
                "                    \"file\": {\n" +
                "                      \"media_id\": \"MEDIA_ID\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.DING.getCode() + H + MessageTypeEnum.DING_LINK.getCode(), "{\n" +
                "                  \"dingUserIdType\": \"userid_list 或者 dept_id_list 或者 userIds 或者 openConversationId\",\n" +
                "                  \"pushSubject\": \"workNotice 或者 robot\",\n" +
                "                  \"msgParam\": {\n" +
                "                    \"text\": \"消息内容测试\",\n" +
                "                    \"title\": \"sampleLink消息测试\",\n" +
                "                    \"picUrl\": \"@lADOADmaWMzazQKA\",\n" +
                "                    \"messageUrl\": \"http://dingtalk.com\"\n" +
                "                  },\n" +
                "                  \"msg\": {\n" +
                "                    \"link\": {\n" +
                "                      \"messageUrl\": \"http://s.dingtalk.com/market/dingtalk/error_code.php\",\n" +
                "                      \"picUrl\": \"@lALOACZwe2Rk\",\n" +
                "                      \"title\": \"测试\",\n" +
                "                      \"text\": \"测试\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.DING.getCode() + H + MessageTypeEnum.DING_OA.getCode(), "{\n" +
                "                  \"dingUserIdType\": \"userid_list 或者 dept_id_list\",\n" +
                "                  \"pushSubject\": \"workNotice\",\n" +
                "                  \"msg\": {\n" +
                "                    \"oa\": {\n" +
                "                      \"message_url\": \"http://dingtalk.com\",\n" +
                "                      \"head\": {\n" +
                "                        \"bgcolor\": \"FFBBBBBB\",\n" +
                "                        \"text\": \"头部标题\"\n" +
                "                      },\n" +
                "                      \"body\": {\n" +
                "                        \"title\": \"正文标题\",\n" +
                "                        \"form\": [\n" +
                "                          {\n" +
                "                            \"key\": \"姓名:\",\n" +
                "                            \"value\": \"黑子你家哥哥\"\n" +
                "                          },\n" +
                "                          {\n" +
                "                            \"key\": \"年龄:\",\n" +
                "                            \"value\": \"20\"\n" +
                "                          },\n" +
                "                          {\n" +
                "                            \"key\": \"身高:\",\n" +
                "                            \"value\": \"1.8米\"\n" +
                "                          },\n" +
                "                          {\n" +
                "                            \"key\": \"体重:\",\n" +
                "                            \"value\": \"130斤\"\n" +
                "                          },\n" +
                "                          {\n" +
                "                            \"key\": \"学历:\",\n" +
                "                            \"value\": \"本科\"\n" +
                "                          },\n" +
                "                          {\n" +
                "                            \"key\": \"爱好:\",\n" +
                "                            \"value\": \"唱跳RAP篮球\"\n" +
                "                          }\n" +
                "                        ],\n" +
                "                        \"rich\": {\n" +
                "                          \"num\": \"15.6\",\n" +
                "                          \"unit\": \"元\"\n" +
                "                        },\n" +
                "                        \"content\": \"大段文本大段文本大段文本大段文本大段文本大段文本\",\n" +
                "                        \"image\": \"@lADOADmaWMzazQKA\",\n" +
                "                        \"file_count\": \"3\",\n" +
                "                        \"author\": \"李四 \"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.DING.getCode() + H + MessageTypeEnum.DING_MARKDOWN.getCode(), "{\n" +
                "                  \"dingUserIdType\": \"userid_list 或者 dept_id_list 或者 userIds 或者 openConversationId\",\n" +
                "                  \"pushSubject\": \"workNotice 或者 robot\",\n" +
                "                  \"msgParam\": {\n" +
                "                    \"title\": \"xxxx\",\n" +
                "                    \"text\": \"xxxx\"\n" +
                "                  },\n" +
                "                  \"msg\": {\n" +
                "                    \"markdown\": {\n" +
                "                      \"title\": \"首屏会话透出的展示内容\",\n" +
                "                      \"text\": \"# 这是支持markdown的文本   \\n   ## 标题2    \\n   * 列表1   \\n  ![alt 啊](https://img.alicdn.com/tps/TB1XLjqNVXXXXc4XVXXXXXXXXXX-170-64.png)\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.DING.getCode() + H + MessageTypeEnum.DING_CARD.getCode(), "{\n" +
                "                  \"dingUserIdType\": \"userid_list 或者 dept_id_list 或者 userIds 或者 openConversationId\",\n" +
                "                  \"pushSubject\": \"workNotice 或者 robot\",\n" +
                "                  \"msgParam\": {\n" +
                "                    \"title\": \"测试标题\",\n" +
                "                    \"text\": \"内容测试\",\n" +
                "                    \"singleTitle\": \"查看详情\",\n" +
                "                    \"singleURL\": \"https://open.dingtalk.com\"\n" +
                "                  },\n" +
                "                  \"msg\": {\n" +
                "                    \"action_card\": {\n" +
                "                      \"title\": \"是透出到会话列表和通知的文案\",\n" +
                "                      \"markdown\": \"支持markdown格式的正文内容\",\n" +
                "                      \"single_title\": \"查看详情\",\n" +
                "                      \"single_url\": \"https://open.dingtalk.com\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                }");

        // 企业微信消息参数
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.WECHAT.getCode() + H + MessageTypeEnum.TEXT.getCode(), "{\n" +
                "                  \"pushSubject\": \"app 或者 robot\",\n" +
                "                  \"wechatUserIdType\": \"touser 或者 toparty 或者 totag 或者 to_parent_userid 或者 to_student_userid 或者 to_party 或者 toall 或者 chatid 或者 webhook\",\n" +
                "                  \"text\": {\n" +
                "                    \"content\": \"你的快递已到，请携带工卡前往邮件中心领取。\\n出发前可查看<a href=\\\"http://work.weixin.qq.com\\\">邮件中心视频实况</a>，聪明避开排队。\"\n" +
                "                  },\n" +
                "                  \"safe\": 0,\n" +
                "                  \"enable_id_trans\": 0,\n" +
                "                  \"enable_duplicate_check\": 0,\n" +
                "                  \"duplicate_check_interval\": 1800\n" +
                "                }");
        //TODO:更多参数待补充

        // 飞书消息参数
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.TEXT.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id 或者 department_id\",\n" +
                "                  \"content\": {\n" +
                "                    \"text\": \"test content\"\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.FEI_SHU_POST.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id 或者 department_id\",\n" +
                "                  \"content\": {\n" +
                "                    \"post\": {\n" +
                "                      \"zh_cn\": {\n" +
                "                        \"title\": \"我是一个标题\",\n" +
                "                        \"content\": [\n" +
                "                          [\n" +
                "                            {\n" +
                "                              \"tag\": \"text\",\n" +
                "                              \"text\": \"第一行\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                              \"tag\": \"a\",\n" +
                "                              \"href\": \"http://www.feishu.cn\",\n" +
                "                              \"text\": \"飞书\"\n" +
                "                            }\n" +
                "                          ]\n" +
                "                        ]\n" +
                "                      },\n" +
                "                      \"en_us\": {\n" +
                "                        \"title\": \"I am a title\",\n" +
                "                        \"content\": [\n" +
                "                          [\n" +
                "                            {\n" +
                "                              \"tag\": \"text\",\n" +
                "                              \"text\": \"first line\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                              \"tag\": \"a\",\n" +
                "                              \"href\": \"http://www.feishu.cn\",\n" +
                "                              \"text\": \"feishu\"\n" +
                "                            }\n" +
                "                          ]\n" +
                "                        ]\n" +
                "                      }\n" +
                "                    }\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.FEI_SHU_IMAGE.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id 或者 department_id\",\n" +
                "                  \"content\": {\n" +
                "                    \"image_key\": \"\"\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.FEI_SHU_INTERACTIVE.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id 或者 department_id\",\n" +
                "                  \"card\": {}\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.FEI_SHU_SHARE_CHAT.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id 或者 department_id\",\n" +
                "                  \"content\": {\n" +
                "                    \"chat_id\": \"\"\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.FEI_SHU_SHARE_USER.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id\",\n" +
                "                  \"content\": {\n" +
                "                    \"user_id\": \"\"\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.FEI_SHU_AUDIO.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id\",\n" +
                "                  \"content\": {\n" +
                "                    \"file_key\": \"\"\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.FEI_SHU_MEDIA.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id\",\n" +
                "                  \"content\": {\n" +
                "                    \"file_key\": \"75235e0c-430a-a99b-8446610223cg\",\n" +
                "                    \"image_key\": \"img_xxxxxx\"\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.FEI_SHU_FILE.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id\",\n" +
                "                  \"content\": {\n" +
                "                    \"file_key\": \"75235e0c-430a-a99b-8446610223cg\"\n" +
                "                  }\n" +
                "                }");
        MESSAGE_PARAM_MAP.put(ChannelTypeEnum.FEI_SHU.getCode() + H + MessageTypeEnum.FEI_SHU_STICKER.getCode(), "{\n" +
                "                  \"feiShuUserIdType\": \"user_id 或者 email 或者 chat_id\",\n" +
                "                  \"content\": {\n" +
                "                    \"file_key\": \"75235e0c-430a-a99b-8446610223cg\"\n" +
                "                  }\n" +
                "                }");
    }
}
