package io.tml.iov.common.config;

import io.tml.iov.common.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocalVersionConfig {

    private String version = "2011";

    private ProtocalVersionConfig() {
        try {
            version = PropertiesUtil.getString("protocal.809.version", "2011");
        } catch (Exception e) {
            log.error(
                    "ProtocalVersionConfig init error, Default protocal version -> 2011",
                    e);
        }
    }

    private static class ProtocalVersionConfigHolder {
        private static ProtocalVersionConfig instance = new ProtocalVersionConfig();
    }

    public static ProtocalVersionConfig getInstance() {
        return ProtocalVersionConfigHolder.instance;
    }

    public String getVersion() {
        return version;
    }

}
