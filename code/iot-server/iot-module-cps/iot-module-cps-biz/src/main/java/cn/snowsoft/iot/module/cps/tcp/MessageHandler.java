package cn.snowsoft.iot.module.cps.tcp;

import cn.snowsoft.iot.module.cps.initServer.cache.ServerCache;
import cn.snowsoft.iot.module.cps.emqx.client.PahoClient;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: chen_gang
 * @date: 2024/10/16 18:02
 * @description: 接收消息业务处理类
 */
@RequiredArgsConstructor
public class MessageHandler extends SimpleChannelInboundHandler<JSONObject> {
    private final PahoClient pahoClient;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject msg) throws Exception {
        System.out.println("Received frame: " + msg.toString());
        String deviceCode = msg.getString("deviceCode");
        if (StringUtils.isNotBlank(deviceCode)) {
            String topic = ServerCache.deviceCodeAttributeTopic.get(deviceCode);
            if (StringUtils.isNotBlank(topic)) {
                pahoClient.publish(topic, msg.toString());
                //刷新设备通道对应关系，防止掉线重连后通道更换
                TcpServer.deviceChannelMap.put(deviceCode, ctx.channel());
            }
        }
    }
}