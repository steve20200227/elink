package cn.snowsoft.iot.module.cps.tcp;

import cn.snowsoft.iot.module.cps.initServer.cache.ServerCache;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * @author: chen_gang
 * @date: 2024/10/16 18:01
 * @description: 接收消息-自定义解码
 */
@RequiredArgsConstructor
public class MessageDecoder extends ByteToMessageDecoder {

    private final String agreementCode;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            byte[] array = new byte[in.readableBytes()];
            in.readBytes(array);

            //执行groovy脚本，进行自定义数据包解析
            Binding binding = new Binding();
            binding.setVariable("data", array);
            GroovyShell groovyShell = new GroovyShell(binding);
            Object res = groovyShell.evaluate(ServerCache.protocolAgreement.get(agreementCode));
            String resStr = (String) res;
            JSONArray jsonArray = JSONArray.parseArray(resStr);
            if (ObjectUtils.isNotEmpty(jsonArray)) {
                for (Object o : jsonArray) {
                    out.add(o);
                }
            } else {
                out.add(new JSONObject());
            }
        } catch (Exception e) {
            out.add(new JSONObject());
        }
    }
}
