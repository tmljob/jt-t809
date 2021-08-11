package io.tml.iov.common.packet;

import java.nio.charset.Charset;

import io.tml.iov.common.config.EncryptConfig;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.common.util.Jtt809Util;
import io.tml.iov.common.util.constant.Const;

/**
 *  登录请求包，此中的结尾数据体的消息。
 */

public class JT809LoginPacket extends JT809BasePacket {

    private static final int FIXED_LENGTH = 46;

    public JT809LoginPacket() {
        setMsgLength(getFixedByteLength() + FIXED_LENGTH);
        setMsgSn(Const.getMsgSN());
        setMsgId(Const.BusinessDataType.UP_CONNECT_REQ);
        setMsgGNSSCenterId(Const.UserInfo.MSG_GNSSCENTERID);
//        setVersionFlag(new byte[] { 1, 0, 0 });
        // 加密配置
//        setEncryptFlag((byte) EncryptConfig.getInstance().getEncryptFlag());
//        setEncryptKey(0);
    }

    /** id 4字节 */
    private int userId;
    /** 密码 8字节 */
    private String password;
    /** 下级平台IP 32字节 */
    private String downLinkIp;
    /** 下级平台端口 2字节 */
    private short downLinkPort;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDownLinkIp() {
        return downLinkIp;
    }

    public void setDownLinkIp(String downLinkIp) {
        this.downLinkIp = downLinkIp;
    }

    public short getDownLinkPort() {
        return downLinkPort;
    }

    public void setDownLinkPort(short downLinkPort) {
        this.downLinkPort = downLinkPort;
    }

    @Override
    public byte[] getMsgBodyByteArr() {
        byte[] useridBytes = CommonUtils.int2bytes(userId);
        byte[] pwdBytes = CommonUtils.getBytesWithLengthAfter(8,
                password.getBytes(Charset.forName("GBK")));
        byte[] byte1 = CommonUtils.append(useridBytes, pwdBytes);
        byte[] downIPBytes = CommonUtils.getBytesWithLengthAfter(32,
                downLinkIp.getBytes(Charset.forName("GBK")));
        byte[] portBytes = CommonUtils.short2Bytes(downLinkPort);
        byte[] byte2 = CommonUtils.append(downIPBytes, portBytes);

        byte[] msgBody = CommonUtils.append(byte1, byte2);
        if (this.getEncryptFlag() == Const.SWITCH_ON) {
            msgBody = Jtt809Util.encrypt(EncryptConfig.getInstance().getM1(),
                    EncryptConfig.getInstance().getIa1(),
                    EncryptConfig.getInstance().getIc1(), getEncryptKey(),
                    msgBody);
        }
        return msgBody;
    }

    @Override
    public String toString() {
        return "JT809LoginPacket{" + "userId='" + userId + '\'' + ", password='"
                + password + '\'' + ", downLinkIp='" + downLinkIp + '\''
                + ", downLinkPort='" + downLinkPort + '\'' + super.toString()
                + '}';
    }
}
