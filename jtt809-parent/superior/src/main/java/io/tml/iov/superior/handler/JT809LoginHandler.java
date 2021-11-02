package io.tml.iov.superior.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.tml.iov.common.packet.JT809LoginPacket;
import io.tml.iov.common.packet.JT809LoginResponsePacket;
import io.tml.iov.common.util.PropertiesUtil;
import io.tml.iov.common.util.constant.Const;

public class JT809LoginHandler
        extends SimpleChannelInboundHandler<JT809LoginPacket> {
    private static Logger log = LoggerFactory
            .getLogger(JT809LoginHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
            JT809LoginPacket msg) {
        JT809LoginResponsePacket loginResponsePacket = new JT809LoginResponsePacket();
        byte loginResponseCode = valid(msg);
        loginResponsePacket.setResult(loginResponseCode);
        loginResponsePacket.setVerifyCode(0);

        // 登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    /** 用户名密码校验 */
    private byte valid(JT809LoginPacket msg) {
        int userId = msg.getUserId();
        String password = msg.getPassword().trim();
        log.info("接收到了登录的请求->用户名：{};密码：{};", userId, password);

        int userIdSettings = PropertiesUtil.getInteger("netty.server.userid");
        String pwdSetting = PropertiesUtil.getString("netty.server.pwd");

        if (userIdSettings == userId && pwdSetting.equals(password)) {
            log.info("登录验证成功");
            return Const.LoginResponseCode.SUCCESS;
        } else if (userIdSettings != userId) {
            log.info("USER_ID not exist");
            return Const.LoginResponseCode.USERNAME_ERROR;
        } else {
            log.info("PASSWORD_ERROR");
            return Const.LoginResponseCode.PASSWORD_ERROR;
        }
    }

}
