package io.tml.iov.superior.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.packet.JT809HeartbeatResponse;
import io.tml.iov.common.util.constant.Const;

/**
 * @Author: Xiuming Lee
 * @Date: 2019/9/23 14:42
 * @Version 1.0
 * @Describe:
 */
public class JT809HeartbeatHandler extends SimpleChannelInboundHandler<JT809HeartbeatResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JT809HeartbeatResponse msg) throws Exception {
        msg.setMsgLength(JT809BasePacket.getFixedByteLength());
        msg.setMsgSn(Const.getMsgSN());
        msg.setMsgId(Const.BusinessDataType.UP_LINKTEST_RSP);
        msg.setMsgGNSSCenterId(Const.UserInfo.MSG_GNSSCENTERID);
        msg.setVersionFlag(new byte[]{1,0,0});
        msg.setEncryptFlag(Const.EncryptFlag.NO);
        msg.setEncryptKey(0);
        ctx.channel().writeAndFlush(msg);
    }
}
