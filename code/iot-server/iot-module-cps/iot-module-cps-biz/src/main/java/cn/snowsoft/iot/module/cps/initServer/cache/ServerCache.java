package cn.snowsoft.iot.module.cps.initServer.cache;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.snowsoft.iot.module.cps.config.SpringContextConfig;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import cn.snowsoft.iot.module.cps.emqx.api.RuleApi;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import cn.snowsoft.iot.module.cps.service.equipment.EquipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: raft
 * @date: 2023/2/6 15:07
 * @description: 从mysql加载必要数据缓存到内存中
 */
@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn("emqxProperties")
public class ServerCache {

	private final EquipmentService equipmentService;

	//内存模式存储设备的 设备编码和属性上报topic映射关系，tcp协议解析消息时使用
	public static ConcurrentHashMap<String, String> deviceCodeAttributeTopic = new ConcurrentHashMap<>();

	//内存模式存储设备的 设备编码和功能下发topic映射关系，tcp协议解析消息时使用
	public static ConcurrentHashMap<String, String> deviceCodeFeatureTopic = new ConcurrentHashMap<>();

	//内存模式存储设备的 设备编码和ID映射关系，定时任务调用设备功能下发时使用
	public static ConcurrentHashMap<String, Long> deviceCodeID = new ConcurrentHashMap<>();

	//内存模式存储接入协议的 协议编码和脚本代码映射关系，tcp协议解析消息时使用
	public static ConcurrentHashMap<String, String> protocolAgreement = new ConcurrentHashMap<>();

	//内存模式存储开启了数据持久化功能的设备的属性上报topic，用于设备启动失效时进行更新emqx规则
	public static ConcurrentHashSet<String> historySet = new ConcurrentHashSet();
	@PostConstruct
	private void process() {
		List<EquipmentDO> equipmentDOList = equipmentService.lambdaQuery()
				.eq(EquipmentDO::getIsEnable, "1")
				.list();
		if (ObjectUtils.isNotEmpty(equipmentDOList)) {
			equipmentDOList.stream().forEach(e -> {
				//启用则缓存到集合
				if ("1".equals(e.getIsHistory())) {
					historySet.add(e.getAttributePush());
				}
				deviceCodeAttributeTopic.put(e.getEquipmentCode(), e.getAttributePush());
				deviceCodeFeatureTopic.put(e.getEquipmentCode(), e.getFeatureIssued());
				deviceCodeID.put(e.getEquipmentCode(), e.getId());
			});
		}
	}

	/**
	 * 更新emqx中设备属性上报持久化规则sql，主要实现控制不需要持久化设备的topic过滤
	 */
	public static void updateAttributeRabbitBridgeHistory() {
		EmqxProperties emqxProperties = SpringContextConfig.getBean(EmqxProperties.class);
		//修改历史数据规则
		Map<String, Object> map = new HashMap<>();
		ConcurrentHashSet<String> historySet = ServerCache.historySet;
		StringBuilder sb = new StringBuilder("");
		//计数器
		int count = 0;
		for (String s : historySet) {
			count++;
			//如果是最后一个元素 不需要逗号
			if (count != historySet.size()){
				sb.append("'"+s+"',");
			}else {
				sb.append("'"+s+"'");
			}
		}
		String sql = emqxProperties.getRulesHistory().replace("@{params}",sb.toString());

		map.put("sql", sql);
        try {
            RuleApi.updateRules(map, "attribute_rabbit_bridge_history");
        } catch (IOException e) {
            log.error("EMQX规则初始化失败 attribute_rabbit_bridge_history   {}", e.getMessage());
        }
    }
}
