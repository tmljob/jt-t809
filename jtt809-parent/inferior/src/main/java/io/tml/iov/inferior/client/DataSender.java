package io.tml.iov.inferior.client;

import org.apache.commons.lang3.StringUtils;

import io.tml.iov.common.packet.JT809LoginPacket;
import io.tml.iov.common.packet.JT809Packet0x1202;
import io.tml.iov.common.util.PropertiesUtil;
import io.tml.iov.common.util.constant.Const;
import io.tml.iov.inferior.client.constant.Constants;
import io.tml.iov.inferior.client.util.IpUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSender {

    /**
     * 交委指定本公司接入码
     */
    private  int platCode = PropertiesUtil
            .getInteger("netty.server.centerId");
    /**
     * 交委指定本公司用户名
     */
    private int comId = PropertiesUtil.getInteger("netty.server.userid");
    /**
     * 交委指定本公司密码
     */
    private String comPwd = PropertiesUtil.getString("netty.server.pwd");
  
    private String downLinkIp = "127.0.0.1";
    
    private  String loginStatus = "";

    private int loginFlag = 0;
    
    private static TCPClient809 tcpclient = TCPClient809.getInstance();

    public static final  String LOGINING = "logining";
    public static final int M1 = PropertiesUtil.getInteger("superior.server.m1");
    public static final int IA1 = PropertiesUtil.getInteger("superior.server.ia1");
    public static final int IC1 = PropertiesUtil.getInteger("superior.server.ic1");

    private static DataSender dataSender = new DataSender();

    public static DataSender getInstance() {
        return dataSender;
    }
    
    private DataSender() {
        // 获取本机IP对应的用户名密码,IpUtils自己实现一个，就是获取本地IP的，因为有的城市的交委会给每个服务器一个账号密码
        String localIp = IpUtils.getIpAddress();
        if (StringUtils.isNotBlank(localIp)) {
            platCode = PropertiesUtil.getInteger("netty.server.centerId");
            comId = PropertiesUtil.getInteger("netty.server.userid");
            comPwd = PropertiesUtil.getString("netty.server.pwd");

            downLinkIp = localIp;
        } else {
            log.error("获取本机IP异常");
        }
        
        tcpclient.doConnect();
    }

    /**
     * 判断是否登录 boolean
     */
    public  boolean isLogined() {
        return Constants.LOGIN_SUCCESS.equals(loginStatus);
    }

    /**
     * 登录上级平台 boolean
     */
    public  boolean login2Superior() {

        boolean success = false;
        if (!Constants.LOGIN_SUCCESS.equals(loginStatus)
                && !LOGINING.equals(loginStatus)) {
            // 开始登录
            JT809LoginPacket loginPacket = new JT809LoginPacket();
            loginPacket.setMsgId((short) Const.BusinessDataType.UP_CONNECT_REQ);
            loginPacket.setUserId(comId);
            loginPacket.setPassword(comPwd);
            loginPacket.setDownLinkIp(downLinkIp);
            loginPacket.setDownLinkPort(
                    (short) PropertiesUtil.getInteger("downlink.port"));
            loginPacket.setMsgGNSSCenterId(platCode);
            tcpclient.getChannel().write(loginPacket);
            loginStatus = LOGINING;
        }
        return success;
    }

    /**
     * 发送数据到交委接入平台 boolean
     *
     * @param awsVo
     * @return 2016年9月28日 by fox_mt
     */
    public  boolean sendMsg2Gov(JT809Packet0x1202 awsVo) {
        boolean success = false;
        if (isLogined()) {
            // 已经登录成功，开始发送数据
            if (channelAvaliable()) {
                tcpclient.getChannel().write(awsVo);
                success = true;
                log.info("send -->" + awsVo.toString());
            } else {
                success = false;
                loginStatus = "";
            }
        } else if (loginFlag == 0) {
            loginFlag++;
            login2Superior();
            log.error("--------------第一次登录");
        } else {
            log.error("--------------等待登录");
        }
        return success;
    }

    public  boolean channelAvaliable() {
        return null != tcpclient.getChannel()
                && tcpclient.getChannel().isWritable();
    }
    
    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
    
    public String getLoginStatus() {
        return loginStatus;
    }


}
