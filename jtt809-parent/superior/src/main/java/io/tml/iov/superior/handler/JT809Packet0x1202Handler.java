package io.tml.iov.superior.handler;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.tml.iov.common.packet.JT809Packet0x1202;
import io.tml.iov.common.util.PropertiesUtil;
import io.tml.iov.superior.bean.LocationMsg;
import io.tml.iov.superior.config.KafkaProducerConfig;

public class JT809Packet0x1202Handler
        extends SimpleChannelInboundHandler<JT809Packet0x1202> {
    private static Logger log = LoggerFactory
            .getLogger(JT809Packet0x1202Handler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
            JT809Packet0x1202 msg) {
        log.info("车辆定位信息：{}", msg.toString());
        if (KafkaProducerConfig.getInstance().isEnable()) {
            // 1.创建一个生产者对象
            Producer<String, String> producer = new KafkaProducer<>(
                    KafkaProducerConfig.getInstance().getProperties());
            ObjectMapper objectMapper = new ObjectMapper();
            // 2.调用send方法
            try {
                producer.send(
                        new ProducerRecord<String, String>(
                                PropertiesUtil.getString("kafka.topic"), null,
                                objectMapper.writeValueAsString(
                                        LocationMsg.convert(msg))),
                        (metadata, exception) -> {
                            if (exception == null) {
                                log.info("消息发送成功->{}", metadata.offset());
                            } else {
                                log.error("消息发送失败->{}", exception.getMessage(),
                                        exception);
                            }
                        });
            } catch (Exception e) {
                log.error("kafka send location msg error.", e);
            } finally {
                // 3.关闭生产者
                producer.close();
            }
        }
    }
}
