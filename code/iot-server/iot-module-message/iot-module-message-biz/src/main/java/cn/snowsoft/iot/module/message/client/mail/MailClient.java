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

package cn.snowsoft.iot.module.message.client.mail;


import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.model.app.MailApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 渠道-邮箱工具类
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
public class MailClient {

    /**
     * 发送邮件
     *
     * @param mailApp     邮件应用
     * @param sendTaskDto dto
     */
    public void sendMail(MailApp mailApp, SendTaskDto sendTaskDto) {
        try {
            Map<String, Object> paramMap = sendTaskDto.getParamMap();
            String title = paramMap.get("title").toString();
            String content = paramMap.get("content").toString();
            List<String> toCC = new ArrayList<>();
            List<String> toBCC = new ArrayList<>();
            if ("".equals(paramMap.get("toCC").toString())){
                toCC = new ArrayList<>();
            }else {
                toCC = (List<String>) paramMap.get("toCC");
            }
            if ("".equals(paramMap.get("toBCC").toString())){
                toBCC = new ArrayList<>();
            }else {
                toBCC = (List<String>) paramMap.get("toBCC");
            }

            Boolean htmlFlag = (Boolean) paramMap.get("htmlFlag");

            //1. 创建 JavaMailSender
            JavaMailSenderImpl javaMailSender = getJavaMailSender(mailApp);

            //2. 创建 message 对象
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailApp.getUsername());
            helper.setTo(sendTaskDto.getUsers().toArray(new String[0]));
            if (toCC!=null){
                helper.setCc(toCC.toArray(new String[0]));
            }
            if (toBCC!=null){
                helper.setBcc(toBCC.toArray(new String[0]));
            }

            helper.setSubject(title);
            helper.setText(content, htmlFlag);
            // TODO：增加邮件附件
            // helper.addAttachment("", new File(""));

            // 3. 发送邮件
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new MessageException(sendTaskDto, "发送邮件失败，" + e.getMessage());
        }
        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "邮件消息发送成功");
    }

    /**
     * 获取 JavaMailSender
     *
     * @param mailApp 邮件应用
     * @return JavaMailSender
     */
    private static JavaMailSenderImpl getJavaMailSender(MailApp mailApp) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(mailApp.getUsername());
        javaMailSender.setPassword(mailApp.getPassword());
        javaMailSender.setHost(mailApp.getHost());
        javaMailSender.setDefaultEncoding("UTF-8");
        javaMailSender.setProtocol("smtp");
        Properties pro = new Properties();
        pro.put("mail.smtp.auth", mailApp.getAuth());
        pro.put("mail.smtp.ssl.enable", mailApp.getSslEnable());
        pro.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailSender.setJavaMailProperties(pro);
        return javaMailSender;
    }

}
