package cn.snowsoft.iot.module.cps.service.cpsRule;

import cn.hutool.core.util.ObjectUtil;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsRulePageVO;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsActionOutputVO;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsRuleSaveVO;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.DeviceProductVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.cpsRule.CpsRule;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import cn.snowsoft.iot.module.cps.dal.mysql.cpsRule.CpsRuleServiceMapper;
import cn.snowsoft.iot.module.cps.emqx.api.RuleApi;
import cn.snowsoft.iot.module.cps.service.equipment.EquipmentService;
import cn.snowsoft.iot.module.cps.service.product.ProductService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class CpsRuleServiceImpl extends ServiceImpl<CpsRuleServiceMapper, CpsRule> implements CpsRuleService {
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ProductService productService;
    /**
     * 删除规则
     *
     * @param rulesCode
     * @return
     */
    @Override
    public Boolean mqttDeleteRules(String rulesCode) {
        try {
            //删除规则
            RuleApi.deleteRules(rulesCode);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    /**
     * 查询列表规则
     *
     * @param cpsRulePageVO
     * @return
     */
    @Override
    public IPage<CpsRulePageVO> selectcpsRulePage(CpsRulePageVO cpsRulePageVO) {
        IPage<EquipmentPointDO> page = new Page<>(cpsRulePageVO.getPageNo(), cpsRulePageVO.getPageSize());
        return baseMapper.selectCpsRulePage(page, cpsRulePageVO);
    }

    /**
     * 设备启用或者失效时，对设备的三个topic转换规则进行生成和删除
     *
     * @param isEnabled
     * @return
     * @throws IOException
     */
    @Override
    public Boolean enabledOrDisabledRules(List<Long> equipmentId, boolean isEnabled) {
        //根据设备id集合查询出这些设备的相关规则
        List<CpsRule> cpsRuleList = lambdaQuery().in(CpsRule::getRelevanceId, equipmentId).list();
        //判断是否启用
        if (isEnabled) {
            List<CpsRule> cpsRuleArrayList = new ArrayList<>();
            //遍历需要处理的规则集合
            cpsRuleList.stream().forEach(e -> {
                //如果当前规则的sql或者动作是空的，那么跳过新增处理
                if (StringUtils.isBlank(e.getRuleSql()) || StringUtils.isBlank(e.getAction())) {
                    return;
                }
                CpsRule cpsRule = new CpsRule();
                //如果规则编码为空，则生成随机编码，以Rule-开头
                if (ObjectUtil.isEmpty(e.getRuleCode())) {
                    e.setRuleCode("Rule-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
                }
                BeanUtils.copyProperties(e, cpsRule);
                cpsRuleArrayList.add(cpsRule);
                //创建规则
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    //转换动作字段的值为EMQX接口需要的格式
                    List<CpsActionOutputVO> cpsActionOutputVOS = new ArrayList<>();
                    if (ObjectUtils.isNotEmpty(e.getAction())) {
                        cpsActionOutputVOS = objectMapper.readValue(e.getAction(), new TypeReference<List<CpsActionOutputVO>>() {
                        });
                    }
                    RuleApi.createRule((transformationStructure(e, cpsActionOutputVOS)));
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            //批量更新这些规则，主要是将之前没有编码的规则进行编码更新
            return updateBatchById(cpsRuleArrayList);
        } else {
            //遍历需要处理的规则集合
            cpsRuleList.stream().forEach(e -> {
                if (StringUtils.isNotBlank(e.getRuleCode())) {
                    try {
                        RuleApi.deleteRules(e.getRuleCode());
                    } catch (IOException ex) {
                        log.error("设备ID: {},转换规则：{},删除失败 {}", e.getRelevanceId(), e.getRuleCode(), ex.getMessage());
                    }
                }
            });
            return true;
        }
    }

    /**
     * 生成随机的 10 位数字 UUID
     *
     * @return
     */
    public static String generateRandom10DigitNumber() {
        StringBuilder sb = new StringBuilder(10);
        Random random = new Random();

        // 生成 10 位随机数字
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10)); // 生成 0 到 9 的随机数字
        }

        return sb.toString(); // 返回生成的 10 位数字
    }

    /**
     * 设备接入认证的转换功能保存调用
     *
     * @param deviceProductVO
     */
    @Override
    public void saveOrUpdateRule(DeviceProductVO deviceProductVO) {
        //获取转换配置信息
        CpsRuleSaveVO cpsRuleSaveVO = deviceProductVO.getCpsRuleSaveVO();
        cpsRuleSaveVO.setIsEnable("1");
        //规则数据
        cpsRuleSaveVO.setRelevanceId(deviceProductVO.getId());
        cpsRuleSaveVO.setRuleInput("");
        cpsRuleSaveVO.setAction("");
        if (ObjectUtils.isNotEmpty(cpsRuleSaveVO.getCpsDataEntries())) {
            cpsRuleSaveVO.getCpsDataEntries().stream().forEach(e -> e.setId(generateRandom10DigitNumber()));
            cpsRuleSaveVO.setRuleInput(JSON.toJSONString(cpsRuleSaveVO.getCpsDataEntries()));
        }
        if (ObjectUtils.isNotEmpty(cpsRuleSaveVO.getCpsActionOutputs())) {
            cpsRuleSaveVO.getCpsActionOutputs().stream().forEach(e -> e.setId(generateRandom10DigitNumber()));
            //动作输出
            cpsRuleSaveVO.setAction(JSON.toJSONString(cpsRuleSaveVO.getCpsActionOutputs()));
        }
        saveOrUpdate(cpsRuleSaveVO);
    }

    /**
     * 根据传入的规则参数，进行EMQX规则生成接口的数据结构化
     * @param one 规则记录
     * @param cpsActionOutputList 规则的动作集合
     * @return
     */
    private JSONObject transformationStructure(CpsRule one, List<CpsActionOutputVO> cpsActionOutputList) {
        JSONObject map = new JSONObject();
        map.put("name", "");
        map.put("id", one.getRuleCode());
        map.put("sql", one.getRuleSql());
        map.put("enable", true);
        //备注
        map.put("description", "");
        map.put("metadata", new HashMap<>());
        List<Object> list = new ArrayList<>();
        //构建动作列表
        if (ObjectUtils.isNotEmpty(cpsActionOutputList)) {
            for (CpsActionOutputVO action : cpsActionOutputList) {
                Map<String, Object> republishAction = new HashMap<>();
                Map<String, String> args = new HashMap<>();
                args.put("payload", ObjectUtils.isNotEmpty(action.getPayload()) ? action.getPayload() : "${payload}");
                args.put("topic", action.getActionTheme());
                republishAction.put("args", args);
                republishAction.put("function", "republish");
                list.add(republishAction);
            }
        }
        // 将 actions 添加到主 Map
        map.put("actions", list);
        return map;
    }
}