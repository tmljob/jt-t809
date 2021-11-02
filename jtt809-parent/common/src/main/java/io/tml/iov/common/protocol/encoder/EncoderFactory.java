package io.tml.iov.common.protocol.encoder;

import java.util.HashMap;
import java.util.Map;


public class EncoderFactory {
    
    private EncoderFactory() {
    }
    
    private static Map<String,Encoder> factMap = new HashMap<>();
    static {
        factMap.put("JT809LoginResponsePacket",new LoginResponseEncoder());
    }

    public static Encoder getEncoder(String packetClassName){
        return factMap.get(packetClassName);
    }
}
