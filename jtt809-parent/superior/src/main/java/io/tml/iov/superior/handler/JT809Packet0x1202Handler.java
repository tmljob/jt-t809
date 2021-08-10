package io.tml.iov.superior.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.tml.iov.common.packet.JT809Packet0x1202;

/**
 * @Author: Xiuming Lee
 * @Date: 2019/9/23 16:04
 * @Version 1.0
 * @Describe:
 */
public class JT809Packet0x1202Handler extends SimpleChannelInboundHandler<JT809Packet0x1202> {
    private static Logger log = LoggerFactory.getLogger(JT809Packet0x1202Handler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JT809Packet0x1202 msg) {
        log.info("车辆定位信息：{}", msg.toString());
//        if (msg.getVec1() > 0 || msg.getVec2() > 0) {
//            JT809Dao.insert0x1202(msg);
//        log.info("{}", msg);
//        }
    }
}
