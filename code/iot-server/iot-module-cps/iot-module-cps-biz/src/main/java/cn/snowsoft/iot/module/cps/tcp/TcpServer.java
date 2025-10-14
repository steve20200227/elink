package cn.snowsoft.iot.module.cps.tcp;

import cn.snowsoft.iot.module.cps.dal.dataobject.analyzeAgreement.AnalyzeAgreementDO;
import cn.snowsoft.iot.module.cps.initServer.cache.ServerCache;
import cn.snowsoft.iot.module.cps.emqx.client.PahoClient;
import cn.snowsoft.iot.module.cps.service.analyzeAgreement.AnalyzeAgreementService;
import io.netty.channel.Channel;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: chen_gang
 * @date: 2024/10/16 17:55
 * @description: TCP服务端，供外部设备连接
 */
@Component
@ConditionalOnProperty(prefix = "tcp", name = "enable", havingValue = "true")
public class TcpServer implements ApplicationRunner {

    @Resource
    private AnalyzeAgreementService analyzeAgreementService;

    @Resource
    private PahoClient pahoClient;

    private ExecutorService executorService;

    private Map<String, TcpServerEndPoint> tcpServerEndPointMap;

    public static ConcurrentHashMap<String, Channel> deviceChannelMap = new ConcurrentHashMap<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 服务启动之后，查询接入协议表，根据协议接入地址依次创建tcpserver接入点
        List<AnalyzeAgreementDO> analyzeAgreementDoList = analyzeAgreementService.lambdaQuery().list();
        if (ObjectUtils.isNotEmpty(analyzeAgreementDoList)) {
            //初始化线程池
            executorService = Executors.newFixedThreadPool(3);
            //初始化端点集合，key：协议编码   value：协议对应的端点对象
            tcpServerEndPointMap = new HashMap<>();

            for (AnalyzeAgreementDO analyzeAgreementDO : analyzeAgreementDoList) {
                TcpServerEndPoint temp = new TcpServerEndPoint(
                        pahoClient,
                        analyzeAgreementDO.getAgreementCode(),
                        Integer.parseInt(analyzeAgreementDO.getAccessAddress().split(":")[1]));
                executorService.submit(temp);
                tcpServerEndPointMap.put(analyzeAgreementDO.getAgreementCode(), temp);
                // 将协议的编码和脚本映射到全局变量中，在协议解析时获取并执行脚本
                ServerCache.protocolAgreement.put(analyzeAgreementDO.getAgreementCode(), analyzeAgreementDO.getCustomAgreement());
            }
        }
    }
}
