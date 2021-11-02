package io.tml.iov.common.util.constant;

/**
 * 一些常量
 */
public class Const {
    /** 报文序列号 */
    private static int msgSn = -1;

    public static int getMsgSN() {
        msgSn += 1;
        return msgSn;
    }

    public static final int SWITCH_ON = 1;

    /** 业务数据类型标志 */
    public final class BusinessDataType {
        private BusinessDataType() {
        }
        /** 主链路登录请求消息 */
        public static final short UP_CONNECT_REQ = 0x1001;
        /** 主链路登录应答消息 */
        public static final short UP_CONNECT_RSP = 0x1002;
        /** 主链路连接保持请求消息 */
        public static final short UP_LINKTEST_REQ = 0x1005;
        /** 主链路连接保持应答消息 */
        public static final short UP_LINKTEST_RSP = 0x1006;
        /** 主链路动态信息交换消息 */
        public static final short UP_EXG_MSG = 0x1200;
    }

    /** 子业务数据类型标志 */
    public final class SubBusinessDataType {
        private SubBusinessDataType() {
        }
        /** 实时上传车辆定位信息 */
        public static final short UP_EXG_MSG_REAL_LOCATION = 0x1202;
    }

    /** 主链路登录应答 */
    public final class LoginResponseCode {
        private LoginResponseCode() {
        }
        /** 0x00:成功; */
        public static final byte SUCCESS = 0x00;
        /** 0x01:IP地址不正确； */
        public static final byte IP_ERROR = 0x01;
        /** 0x02:接入码不正确； */
        public static final byte ACCESS_CODE_ERROR = 0x02;
        /** 0x03:用户没用注册； */
        public static final byte USERNAME_ERROR = 0x03;
        /** 0x04:密码错误; */
        public static final byte PASSWORD_ERROR = 0x04;
        /** 0x05:资源紧张，稍后再连接(已经占用）； */
        public static final byte RESOURCE_CRISIS = 0x05;
        /** 0x06：其他。 */
        public static final byte OTHER_ERROR = 0x06;
    }

    /** 报文数据体是否加密 */
    public final class Encrypt {
        private Encrypt() {
        }
        /** 加密 */
        public static final byte YES = 0x01;
        /** 不加密 */
        public static final byte NO = 0x00;
        
        public static final int ENCRYPTKEY_LEN = 6;
    }

    /** 协议版本 **/
    public final class ProtocalVersion {
        private ProtocalVersion() {
        }
        public static final String VERSION_2011 = "2011";
        public static final String VERSION_2019 = "2019";
    }
    
    /** 位置附件信息ID **/
    public final class LocAttachInfo {
        private LocAttachInfo() {
        }
        /** 里程  **/
        public static final byte MILE = 0x01;
    }

}
