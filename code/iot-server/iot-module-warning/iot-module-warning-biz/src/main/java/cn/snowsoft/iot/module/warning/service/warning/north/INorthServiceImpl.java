package cn.snowsoft.iot.module.warning.service.warning.north;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.ActionTypeCountVO;
import cn.snowsoft.iot.module.warning.controller.admin.north.vo.NorthPageVO;
import cn.snowsoft.iot.module.warning.controller.admin.north.vo.NorthSaveOrUpdateVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.HTTPDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.MQTTDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.NorthDO;
import cn.snowsoft.iot.module.warning.dal.mysql.warning.north.NorthMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class INorthServiceImpl extends ServiceImpl<NorthMapper, NorthDO>  implements INorthService {


    @Override
    public CommonResult saveUpdate(NorthSaveOrUpdateVO action) {
        NorthDO northDO = new NorthDO();
        BeanUtils.copyProperties(action, northDO);
        // 判断动作类型
        if ("3".equals(northDO.getActionType())) {
            JSONObject jsonObject = new JSONObject();
            if (northDO.getOutputWay().equals("MQTT")) {
                MQTTDO mqtt = action.getMqtt();
                jsonObject.put("mqttAddress", mqtt.getMqttAddress());
                jsonObject.put("clsId", mqtt.getClsId());
                jsonObject.put("userName", mqtt.getUserName());
                jsonObject.put("passWord", mqtt.getPassWord());
                jsonObject.put("qos", mqtt.getQos());
                jsonObject.put("retain", mqtt.getRetain());
                jsonObject.put("theme", mqtt.getTheme());
                String jsonString = jsonObject.toJSONString();
                northDO.setRelatedParameter(jsonString);
                saveOrUpdate(northDO);
                return CommonResult.success(action);
            }
            if (northDO.getOutputWay().equals("HTTP")) {
                HTTPDO http = action.getHttp();
                jsonObject.put("requestPath", http.getRequestPath());
                jsonObject.put("requestMethod", http.getRequestMethod());
                jsonObject.put("requestBody", http.getRequestBody());
                jsonObject.put("requestHeaders", http.getRequestHeaders());
                jsonObject.put("requestParams", http.getRequestParams());
                String jsonString = jsonObject.toJSONString();
                northDO.setRelatedParameter(jsonString);
                saveOrUpdate(northDO);
                return CommonResult.success(action);
            }
        }
        //发送消息保存逻辑
        if("1".equals(northDO.getActionType())){
            saveOrUpdate(northDO);
            return CommonResult.success(northDO);
        }
        return null;
    }

    @Override
    public IPage<NorthDO> selectWarningPage(NorthPageVO actionPage) {
        IPage<NorthDO> page = new Page<>(actionPage.getPageNo(), actionPage.getPageSize());
        return baseMapper.selectActionPage(page, actionPage);
    }

    @Override
    public Boolean removeAction(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public CommonResult getActionById(Long id) {
        // 获取动作原始数据
        NorthDO actionDO = getById(id);
        NorthSaveOrUpdateVO warningAction = new NorthSaveOrUpdateVO();
        if ("3".equals(actionDO.getActionType())) {
            ObjectMapper mapper = new ObjectMapper();
            if (actionDO.getOutputWay().equals("MQTT")) {
                try {
                    MQTTDO mqttdo = mapper.readValue(actionDO.getRelatedParameter(), MQTTDO.class);
                    BeanUtils.copyProperties(actionDO, warningAction);
                    warningAction.setMqtt(mqttdo);
                    return CommonResult.success(warningAction);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            if (actionDO.getOutputWay().equals("HTTP")) {
                try {
                    HTTPDO httpdo = mapper.readValue(actionDO.getRelatedParameter(), HTTPDO.class);
                    BeanUtils.copyProperties(actionDO, warningAction);
                    warningAction.setHttp(httpdo);
                    return CommonResult.success(warningAction);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if ("1".equals(actionDO.getActionType())){

            BeanUtils.copyProperties(actionDO, warningAction);
            if (StringUtils.isNotEmpty(actionDO.getRelatedParameter())){
                JSONObject jsonObject = JSONObject.parseObject(actionDO.getRelatedParameter());
                warningAction.setMessage(jsonObject);
            }

        }

        return CommonResult.success(warningAction);
    }

    @Override
    public List<ActionTypeCountVO> getActionTypeCount() {
        return baseMapper.getActionTypeCount();
    }

    @Override
    public List<NorthDO> getByIds(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return baseMapper.selectList(new LambdaQueryWrapper<NorthDO>().in(NorthDO::getId, list));
    }
}
