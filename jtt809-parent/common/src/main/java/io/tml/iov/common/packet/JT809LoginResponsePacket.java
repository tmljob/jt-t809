package io.tml.iov.common.packet;

import io.tml.iov.common.config.EncryptConfig;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.common.util.Jtt809Util;
import io.tml.iov.common.util.constant.Const;

/**
 * 登录应答包
 */
public class JT809LoginResponsePacket extends JT809BasePacket {

    private static final int FIXED_LENGTH = 5;

    public JT809LoginResponsePacket() {
        setMsgLength(getFixedByteLength() + FIXED_LENGTH);
        setMsgSn(Const.getMsgSN());
        setMsgId(Const.BusinessDataType.UP_CONNECT_RSP);
        setMsgGNSSCenterId(Const.UserInfo.MSG_GNSSCENTERID);
//        setVersionFlag(new byte[] { 1, 0, 0 });
        // 加密配置
//        setEncryptFlag((byte) EncryptConfig.getInstance().getEncryptFlag());
//        setEncryptKey(0);
    }

    /** 标志 1位 */
    private byte result;
    /** 校验码 4字节 */
    private int verifyCode;

    public byte getResult() {
        return result;
    }

    public void setResult(byte resul) {
        this.result = resul;
    }

    public int getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(int verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public byte[] getMsgBodyByteArr() {
        byte[] verifyCodeBytes = CommonUtils.int2bytes(this.verifyCode);
        byte[] msgBody = CommonUtils.append(new byte[] { this.result },
                verifyCodeBytes);
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
        return "JT809LoginResponsePacket{" + "resul=" + result + ", verifyCode="
                + verifyCode + super.toString() + '}';
    }
}
