package io.tml.iov.inferior.client;

import org.apache.commons.lang3.StringUtils;

import io.netty.channel.Channel;
import io.tml.iov.common.packet.JT809LoginPacket;
import io.tml.iov.common.packet.JT809Packet0x1202;
import io.tml.iov.common.util.PropertiesUtil;
import io.tml.iov.common.util.constant.Const;
import io.tml.iov.inferior.client.constant.Constants;
import io.tml.iov.inferior.client.util.IpUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSender {

    private DataSender() {
    }

    /**
     * 交委指定本公司接入码
     */
    public static int PLANT_CODE = PropertiesUtil
            .getInteger("netty.server.centerId");
    /**
     * 交委指定本公司用户名
     */
    public static int COM_ID = PropertiesUtil.getInteger("netty.server.userid");
    /**
     * 交委指定本公司密码
     */
    public static String COM_PWD = PropertiesUtil.getString("netty.server.pwd");

    public static String LONGINSTATUS = "";
    public static String LOGINING = "logining";
    private static int LOGIN_FLAG = 0;
    private static String DOWN_LINK_IP = "127.0.0.1";

    private static TCPClient809 tcpclient = TCPClient809.getInstance();
    private Channel channel = tcpclient.getChannel(
            PropertiesUtil.getString("netty.server.ip"),
            PropertiesUtil.getInteger("netty.server.port"));
    public static int ENCRYPT_FLAG = PropertiesUtil
            .getInteger("message.encrypt.enable");

    public static int M1 = PropertiesUtil.getInteger("superior.server.m1");
    public static int IA1 = PropertiesUtil.getInteger("superior.server.ia1");
    public static int IC1 = PropertiesUtil.getInteger("superior.server.ic1");

    private static DataSender dataSender = new DataSender();

    public static DataSender getInstance() {
        // 获取本机IP对应的用户名密码,IpUtils自己实现一个，就是获取本地IP的，因为有的城市的交委会给每个服务器一个账号密码
        String localIp = IpUtils.getIpAddress();
        if (StringUtils.isNotBlank(localIp)) {
            PLANT_CODE = PropertiesUtil.getInteger("netty.server.centerId");
            COM_ID = PropertiesUtil.getInteger("netty.server.userid");
            COM_PWD = PropertiesUtil.getString("netty.server.pwd");

            DOWN_LINK_IP = localIp;
        } else {
            log.error("获取本机IP异常");
        }
        return dataSender;
    }

    /**
     * 判断是否登录 boolean
     */
    public boolean isLogined() {
        return Constants.LOGIN_SUCCESS.equals(LONGINSTATUS);
    }

    /**
     * 登录上级平台 boolean
     */
    public boolean login2Superior() {

        boolean success = false;
        if (!Constants.LOGIN_SUCCESS.equals(LONGINSTATUS)
                && !LOGINING.equals(LONGINSTATUS)) {
            // 开始登录
            JT809LoginPacket loginPacket = new JT809LoginPacket();
            loginPacket.setMsgId((short) Const.BusinessDataType.UP_CONNECT_REQ);
            loginPacket.setUserId(COM_ID);
            loginPacket.setPassword(COM_PWD);
            loginPacket.setDownLinkIp(DOWN_LINK_IP);
            loginPacket.setDownLinkPort((short) PropertiesUtil.getInteger("downlink.port"));
            loginPacket.setMsgGNSSCenterId(PLANT_CODE);
            channel.write(loginPacket);
            LONGINSTATUS = LOGINING;
        }
        return success;
    }

    /**
     * 发送数据到交委接入平台 boolean
     *
     * @param awsVo
     * @return 2016年9月28日 by fox_mt
     */
    public boolean sendMsg2Gov(JT809Packet0x1202 awsVo) {
        boolean success = false;
        if (isLogined()) {
            // 已经登录成功，开始发送数据
            if (channelAvaliable()) {
                channel.write(awsVo);
                success = true;
                log.info("发送--" + awsVo.toString());
            } else {
                LONGINSTATUS = "";
            }
        } else if (LOGIN_FLAG == 0) {
            LOGIN_FLAG++;
            login2Superior();
            log.error("--------------第一次登录");
        } else {
            log.error("--------------等待登录");
        }
        return success;
    }

    public boolean channelAvaliable() {
        return null != channel && channel.isWritable();
    }

}
