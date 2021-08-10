package io.tml.iov.common.protocol.encoder;

import java.util.HashMap;
import java.util.Map;


public class EncoderFactory {
    private static Map<String,Encoder> ENCODER_FACTORY = new HashMap();
    static {
        ENCODER_FACTORY.put("JT809LoginResponsePacket",new LoginResponseEncoder());
    }

    public static Encoder getEncoder(String packetClassName){
        return ENCODER_FACTORY.get(packetClassName);
    }
}
