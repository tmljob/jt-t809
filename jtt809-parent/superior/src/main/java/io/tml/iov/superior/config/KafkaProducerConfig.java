package io.tml.iov.superior.config;

import java.util.Properties;

import io.tml.iov.common.util.PropertiesUtil;
import io.tml.iov.common.util.constant.Const;

public class KafkaProducerConfig {

    private static final String BOOTSTRAP_SERVERS_CONFIG = "bootstrap.servers";
    private static final String KEY_SERIALIZER_CLASS_CONFIG = "key.serializer";
    private static final String VALUE_SERIALIZER_CLASS_CONFIG = "value.serializer";

    private Properties properties = new Properties();
    private boolean enable;

    private KafkaProducerConfig() {
        enable = PropertiesUtil.getInteger("kafka.enable") == Const.SWITCH_ON;
        if (enable) {
            properties.put(BOOTSTRAP_SERVERS_CONFIG,
                    PropertiesUtil.getString(BOOTSTRAP_SERVERS_CONFIG));
            properties.put(KEY_SERIALIZER_CLASS_CONFIG,
                    PropertiesUtil.getString(KEY_SERIALIZER_CLASS_CONFIG));
            properties.put(VALUE_SERIALIZER_CLASS_CONFIG,
                    PropertiesUtil.getString(VALUE_SERIALIZER_CLASS_CONFIG));

        }
    }

    private static class KafkaProducerConfigHolder {
        private static KafkaProducerConfig instance = new KafkaProducerConfig();
    }

    public static KafkaProducerConfig getInstance() {
        return KafkaProducerConfigHolder.instance;
    }

    public Properties getProperties() {
        return properties;
    }

    public boolean isEnable() {
        return enable;
    }

}
