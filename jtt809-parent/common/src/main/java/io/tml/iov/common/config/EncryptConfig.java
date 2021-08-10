package io.tml.iov.common.config;

import io.tml.iov.common.util.PropertiesUtil;

public class EncryptConfig {
  
    private int encryptFlag;
    private int m1;
    private int ia1;
    private int ic1;

    private EncryptConfig() {
        encryptFlag = PropertiesUtil.getInteger("message.encrypt.enable");
        m1 = PropertiesUtil.getInteger("superior.server.m1");
        ia1 = PropertiesUtil.getInteger("superior.server.ia1");
        ic1 = PropertiesUtil.getInteger("superior.server.ic1");
    }
    
    private static class EncryptConfigHolder {
        private static EncryptConfig instance = new EncryptConfig();
    }

    public static EncryptConfig getInstance() {
      return EncryptConfigHolder.instance;
    }

    public int getM1() {
        return m1;
    }

    public int getIa1() {
        return ia1;
    }

    public int getIc1() {
        return ic1;
    }

    public int getEncryptFlag() {
        return encryptFlag;
    }
    
    

}
