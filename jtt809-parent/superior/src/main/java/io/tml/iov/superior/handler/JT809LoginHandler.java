package io.tml.iov.superior.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.tml.iov.common.packet.JT809LoginPacket;
import io.tml.iov.common.packet.JT809LoginResponsePacket;
import io.tml.iov.common.util.constant.Const;

/**
 * @Author: Xiuming Lee
 * @Date: 2019/9/22 20:06
 * @Version 1.0
 * @Describe:
 */
public class JT809LoginHandler extends SimpleChannelInboundHandler<JT809LoginPacket> {
    private static Logger log = LoggerFactory.getLogger(JT809LoginHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JT809LoginPacket msg){
        JT809LoginResponsePacket loginResponsePacket = new JT809LoginResponsePacket();
        byte LoginResponseCode = valid(msg);
        loginResponsePacket.setResul(LoginResponseCode);
        loginResponsePacket.setVerifyCode(0);
        // 登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    /** 用户名密码校验*/
    private byte valid(JT809LoginPacket msg) {
        int userId =  msg.getUserId();
        String password =  msg.getPassword().trim();
        log.info("接收到了登录的请求->用户名：{};密码：{};",userId,password);
        if (Const.UserInfo.USER_ID == userId && Const.UserInfo.PASSWORD.equals(password)) {
            log.info("登录验证成功");
            return Const.LoginResponseCode.SUCCESS;
        } else if (Const.UserInfo.USER_ID != userId){
            log.info("USER_ID不正确");
            return Const.LoginResponseCode.USERNAME_ERROR;
        } else if (!Const.UserInfo.PASSWORD.equals(password.toString())){
            log.info("PASSWORD_ERROR");
            return Const.LoginResponseCode.PASSWORD_ERROR;
        }else {
            log.info("OTHER_ERROR");
            return Const.LoginResponseCode.OTHER_ERROR;
        }
    }

}
