package cn.snowsoft.iot.module.warning.event;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.snowsoft.iot.framework.common.exception.ErrorCode;
import cn.snowsoft.iot.module.message.api.send.SendApi;
import cn.snowsoft.iot.module.message.api.send.dto.SendMessageRequestDto;
import cn.snowsoft.iot.module.system.api.dict.dto.DictDataRespDTO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningCommonVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningDetailVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.HTTPDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.MQTTDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningActionRecord;
import cn.snowsoft.iot.module.warning.mqtt.MqttClientUtil;
import cn.snowsoft.iot.module.warning.service.warning.record.IWarningActionRecordService;
import cn.snowsoft.iot.module.warning.service.warning.record.IWarningRecordService;
import cn.snowsoft.iot.module.warning.utils.ActionHttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static cn.snowsoft.iot.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @author: chen_gang
 * @date: 2024/8/12 16:09
 * @description: 监听不同事件类型，执行对应业务逻辑
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WarningEventHandler {

    private final IWarningRecordService warningRecordService;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.userName}")
    private String userName;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.maxInflight}")
    private int maxInflight;

    @Value("${mqtt.serverUrl}")
    private String serverUrl;

    private MqttClientUtil mqttClient;
    //消息服务发送接口
    private final SendApi sendApi;

    private final IWarningActionRecordService outPutRecordService;

    private final RedisCacheManager redisCacheManager;

    @Autowired
	private ApplicationEventPublisher eventPublisher;

    /**
     * mysql持久化   条件场景专用
     */
    public boolean handleMysqlAction(JSONObject jsonObject, String uuId, Long id, String warningName) {
        jsonObject.put("uuId", uuId);
        boolean isNewWarning = warningRecordService.handleWarningRecord(jsonObject, id, warningName);
        log.info("mysql持久化动作处理成功，请求内容 {}, 是否新告警 {}", jsonObject, isNewWarning);
        return isNewWarning;
    }

    /**
     * mysql持久化事件监听器   定时和手动场景专用
     */
    @Async("eventAsyncExecutor")
    @EventListener({MysqlWarningEvent.class})
    public void handleMysqlAction(MysqlWarningEvent event) {
        JSONObject data = event.getJsonObject();
        data.put("uuId", event.getUuId());
        warningRecordService.handleWarningRecord(data, event.getWarningType(), event.getWarningName());
        log.info("mysql持久化动作处理成功，请求内容 {}", event);
    }

    /**
     * 消息发送事件监听器
     */
    @Async("eventAsyncExecutor")
    @EventListener({MessageWarningEvent.class})
    public void handleMessageAction(MessageWarningEvent event) {
        //构造消息发送实体
        String params = event.getParams();
        JSONObject data = JSONObject.parseObject(params);
        Long templateId = data.getLong("templateId");
        Map<String, Object> innerMap = data.getInnerMap();
        String uuId = event.getUuId();
        Long actionId = event.getActionId();
        //消息服务仅支持单次单人发送模式，故在此循环发送消息
        //发送前需要根据innerMap集合 解析消息内容中的每一个占位符参数，并且替换为实际平台数据
        Map<String, Object>  finalInnerMap = formatParam(innerMap,event);
        JSONArray userArray = data.getJSONArray("users");
        if (ObjectUtil.isEmpty(userArray)){
            throw exception(new ErrorCode(500,"发送用户不存在"));
        }
        userArray.forEach(user -> {
            List<String> users = new ArrayList();
            users.add(user.toString());
            SendMessageRequestDto requestBody = new SendMessageRequestDto(
                    templateId,
                    users,
                    finalInnerMap,
                    uuId,
                    actionId,
                    0);
            //调用消息服务，进行消息发送
            sendApi.sendMessage(requestBody);
            log.info("消息发送动作处理成功，请求内容 uuID:{} actionId:{} user:{} templateId:{}", uuId, actionId, user, templateId);
        });
    }

    /**
     * mqtt发送事件监听器
     */
    @Async("eventAsyncExecutor")
    @EventListener({MqttWarningEvent.class})
    public void handleMQTTAction(MqttWarningEvent event) {
        //构造mqtt参数对象
        MQTTDO mqttdo = JSONObject.parseObject(event.getParams(), MQTTDO.class);
        //1.建立mqtt连接
        MqttClientUtil temp = new MqttClientUtil(
                StringUtils.isBlank(mqttdo.getClsId()) ? UUID.randomUUID().toString().substring(18) : mqttdo.getClsId(),
                mqttdo.getUserName(),
                mqttdo.getPassWord(),
                mqttdo.getMqttAddress());
        //2.发送消息

        //记录MQTT发送记录
        WarningActionRecord warningActionRecord = new WarningActionRecord();
        //北向输出
        warningActionRecord.setActionType("3");
        warningActionRecord.setPushUrl(mqttdo.getMqttAddress());
        warningActionRecord.setUserName(mqttdo.getUserName());

        warningActionRecord.setActionId(event.getConfigActionDO().getActionId());
        if (ObjectUtils.isNotEmpty(event.getJsonObject())){
            warningActionRecord.setRequestParam(event.getJsonObject().toJSONString());
        }
        if (temp.connect()) {
            boolean publish = temp.publish(mqttdo.getTheme(), event.getJsonObject().toString(), Boolean.getBoolean(mqttdo.getRetain()), mqttdo.getQos());
            //构造记录对象
            warningActionRecord.setCreateTime(LocalDateTime.now());
            warningActionRecord.setWarningRecordId(event.getUuId());

            //1成功  0 失败
            if (publish) {
                warningActionRecord.setOutputStatus(1);
            } else {
                warningActionRecord.setOutputStatus(0);
                warningActionRecord.setFailReason("mqtt发送消息失败");
            }
            temp.closeConnect(1000);
            log.info("mqtt请求动作处理成功，请求内容 {}", event);
        } else {
            warningActionRecord.setOutputStatus(0);
            warningActionRecord.setFailReason("连接mqtt服务失败");
            log.warn("mqtt请求动作处理异常，连接mqtt服务失败 {}", event);
        }
        outPutRecordService.saveOutPutRecord(warningActionRecord);
    }

    /**
     * http请求事件监听器
     */
    @Async("eventAsyncExecutor")
    @EventListener({HttpWarningEvent.class})
    public void handleHttpAction(HttpWarningEvent event) {
        //获取http请求对象
        HTTPDO httpdo = JSONObject.parseObject(event.getParams(), HTTPDO.class);

        WarningActionRecord warningActionRecord = null;


        //根据请求方法进行http请求
        switch (httpdo.getRequestMethod()) {
            case "GET":
                warningActionRecord = ActionHttpUtil.sendGet(httpdo);
                break;
            case "POST":
                warningActionRecord = ActionHttpUtil.sendPost(httpdo, event.getJsonObject());
                break;
            case "PUT":
                warningActionRecord = ActionHttpUtil.sendPut(httpdo, event.getJsonObject());
                break;
            case "DELETE":
                warningActionRecord = ActionHttpUtil.sendDelete(httpdo);
                break;
            default:
                log.warn("http动作处理异常，非法的请求方式 {}", event);
                warningActionRecord.setFailReason("http动作处理异常，非法的请求方式");
                warningActionRecord.setCreateTime(LocalDateTime.now());
                //北向输出
                warningActionRecord.setActionType("3");
                warningActionRecord.setWarningRecordId(event.getUuId());
                warningActionRecord.setPushUrl(httpdo.getRequestPath());
                warningActionRecord.setRequestType(httpdo.getRequestMethod());
                outPutRecordService.saveOutPutRecord(warningActionRecord);
                return;
        }
        warningActionRecord.setCreateTime(LocalDateTime.now());
        warningActionRecord.setActionType("3");
        warningActionRecord.setWarningRecordId(event.getUuId());
        warningActionRecord.setPushUrl(httpdo.getRequestPath());
        warningActionRecord.setRequestType(httpdo.getRequestMethod());
        //设置北向输出id
        warningActionRecord.setActionId(event.getConfigActionDO().getActionId());
        if (ObjectUtils.isNotEmpty(event.getJsonObject())){
            warningActionRecord.setRequestParam(event.getJsonObject().toJSONString());
        }

        outPutRecordService.saveOutPutRecord(warningActionRecord);
        log.info("http动作处理成功，请求内容 {}", event);
    }

    /**
     * 设备反控发送事件监听器
     */
    @Async("eventAsyncExecutor")
    @EventListener({FeatureWarningEvent.class})
    public void handleFeatureAction(FeatureWarningEvent event) {
        // 记录MQTT发送记录
        WarningActionRecord warningActionRecord = new WarningActionRecord();

        // 获取mqtt连接，建立连接  todo 这个连接不能一直进行保持，需要增加一个最大空闲时间检查机制，超过则断连节省资源
        if (ObjectUtils.isEmpty(mqttClient)) {
            mqttClient = new MqttClientUtil(clientId, userName, password, serverUrl);
            mqttClient.connect();
        }
        //保存场景记录关联的UUID
        warningActionRecord.setWarningRecordId(event.getUuId());
        //设备反控
        warningActionRecord.setActionType("2");
        warningActionRecord.setPushUrl(serverUrl);
        // mqtt连接激活时
        if (mqttClient.isConnected()) {
            JSONArray params = JSONArray.parseArray(event.getParams());
            String failReason = "";
            for (Object param : params) {
                JSONObject temp = (JSONObject) param;
                if(!mqttClient.publish(temp.getString("topic"), temp.getString("payload"), false, 1)) {
                    failReason += "推送失败: " + temp.getString("topic") + " 内容[ " + temp.getString("payload") + " ] ";
                }
            }
            //构造记录对象
            warningActionRecord.setCreateTime(LocalDateTime.now());
            warningActionRecord.setUserName(userName);
            //1成功  0 失败
            if (ObjectUtil.isEmpty(failReason)) {
                warningActionRecord.setOutputStatus(1);
            } else {
                warningActionRecord.setOutputStatus(0);
                warningActionRecord.setFailReason(failReason);
            }
            log.info("设备功能调用处理成功，请求内容 {}", event);
        } else {
            warningActionRecord.setOutputStatus(0);
            warningActionRecord.setFailReason("连接mqtt服务失败" + serverUrl);
            log.warn("设备功能调用处理异常，连接mqtt服务失败 {}", event);
        }
        outPutRecordService.saveOutPutRecord(warningActionRecord);
    }


    public Map<String, Object> formatParam(Map<String, Object> paramMap, MessageWarningEvent event){
        JSONObject jsonObject = event.getJsonObject();
        HashMap<String,String> platFormMap = new HashMap();
        //获取缓存中消息模板参数列表，并组合成map<参数名,实际值>
        List<DictDataRespDTO> list = redisCacheManager.getCache("dictCache").get("message_params", List.class);
        for (DictDataRespDTO dictDataDO : list) {
            switch (dictDataDO.getValue()) {
                case "platformName":
                    platFormMap.put("platformName", dictDataDO.getRemark());
                    break;
                case "platformUrl":
                    platFormMap.put("platformUrl", dictDataDO.getRemark());
                    break;
                case "dateTime":
                    platFormMap.put("dateTime", ObjectUtil.isEmpty(jsonObject) ? "" : jsonObject.getString("thetime"));
                    break;
                default:
                    platFormMap.put(dictDataDO.getValue(), ObjectUtil.isEmpty(jsonObject) ? "" : jsonObject.getString(dictDataDO.getValue()));
                    break;
            }
        }

        //获取本次消息包含的参数列表，需要将这个参数列表的每一个参数替换为真正值
        JSONObject templateParam = (JSONObject) paramMap.get("templateParam");
        Map<String, Object> templateParamMap = templateParam.getInnerMap();

        //使用platFormMap进行替换操作
        templateParamMap.entrySet().forEach(entry -> {
            String s = platFormMap.get(entry.getValue().toString());
            templateParamMap.put(entry.getKey(), s);
        });
        String s = JSONObject.toJSONString(templateParamMap);
        switch (paramMap.get("channelType").toString()) {
            // 当动作是 站内信、电话、短信、邮件、钉钉、企业微信、飞书
            case "0" : {
                paramMap.put("templateParam",s);
                break;
            }
            case "1" :
            {
                if ("tencent".equals(paramMap.get("callProvider").toString())){
                    paramMap.put("templateParamSet",s);
                }
                if ("aliyun".equals(paramMap.get("callProvider").toString())){
                    paramMap.put("ttsParam",s);
                }
                break;
            }
            case "2" :
            {
                paramMap.put("templateParam",s);
                break;
            }
            //邮件
            case "3" :
            {
                Map<String, Object> innerMap = JSONObject.parseObject(s).getInnerMap();
                String content = (String) paramMap.get("content");
                String title = (String) paramMap.get("title");
                String formatContent = StrUtil.format(content, innerMap);
                String formatTitle = StrUtil.format(title, innerMap);
                paramMap.put("content", formatContent);
                paramMap.put("title", formatTitle);
                break;
            }
            case "4" :
            case "5" :
            case "6" :
            default:
                break;
        }



        return paramMap;
    }

    /**
     * 用于处理定时场景和手动场景的动作执行事件发布
     * @param warningCommonVO
     */
    public boolean handleCommonAction(WarningCommonVO warningCommonVO) {
        try {
            WarningDetailVO warningDetailVO = warningCommonVO.getWarningDetailVO();
            List<ConfigActionDO> actionDOList = warningDetailVO.getActionDOList();

            //todo 对于定时和手动场景，此处的finalMessage其实是null，这样会导致发送消息和北向输出类型的动作执行时，发送了空内容，这里应该考虑如何接入内容，接入什么内容合适
            JSONObject finalMessage = warningCommonVO.getJsonObject();
            String uuId = UUID.randomUUID().toString();

            actionDOList.forEach(action -> {
                WarningEvent event;
                switch (action.getOutputWay()) {
                    // 当动作是 站内信、电话、短信、邮件、钉钉、企业微信、飞书
                    case "0" :
                    case "1" :
                    case "2" :
                    case "3" :
                    case "4" :
                    case "5" :
                    case "6" :
                        event = new MessageWarningEvent(finalMessage, action.getRelatedParameter(), uuId, action.getId(),warningDetailVO,action);
                        break;

                    case "MQTT" :
                        event = new MqttWarningEvent(finalMessage, action.getRelatedParameter(), uuId, action.getId(),warningDetailVO,action);
                        break;

                    case "HTTP" :
                        event = new HttpWarningEvent(finalMessage, action.getRelatedParameter(), uuId, action.getId(),warningDetailVO,action);
                        break;

                    case "feature" :
                        event = new FeatureWarningEvent(finalMessage, action.getRelatedParameter(), uuId, action.getId(),warningDetailVO,action);
                        break;

                    default:
                        event = null;
                }
                //发布事件
                if (ObjectUtils.isNotEmpty(event)) {
                    eventPublisher.publishEvent(event);
                    log.info("发布事件完成 {}", event);
                }
            });

            //发布mysql持久化事件，进行场景信息留痕
            JSONObject jsonObjectParam = new JSONObject();
            jsonObjectParam.put("warningId",warningDetailVO.getId());
            eventPublisher.publishEvent(new MysqlWarningEvent(jsonObjectParam, uuId, warningDetailVO.getWarningType(), warningDetailVO.getWarningName()));
            return true;
        } catch (Exception e) {
            log.warn("场景动作执行发布事件失败", e.getMessage());
            return false;
        }
    }
}
