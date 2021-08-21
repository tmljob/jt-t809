package io.tml.iov.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesUtil {

    private static Properties props;

    // Tomcat运行时执行
    // 代码块执行顺序：静态代码块>普通代码块>构造代码块
    // 构造代码块每次都执行，但是静态代码块只执行一次
    static {
        String fileName = "application.properties";
        props = new Properties();
        try {
            File configFile = new File(
                    System.getProperty("user.dir") + File.separator + fileName);
            InputStream in = configFile.exists()
                    ? new FileInputStream(configFile)
                    : PropertiesUtil.class.getClassLoader()
                            .getResourceAsStream(fileName);
            props.load(new InputStreamReader(in, "UTF-8"));
        } catch (IOException e) {
            log.error("配置文件读取异常", e);
        }
    }

    // 自定义俩个get方法，方便调用工具类读取properties文件的属性
    public static String getString(String key) {
        String value = props.getProperty(key.trim());
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return value.trim();
    }

    public static String getString(String key, String defaultValue) {
        String value = props.getProperty(key.trim());
        if (Strings.isNullOrEmpty(value)) {
            value = defaultValue;
        }
        return value.trim();
    }

    // 自定义俩个get方法，方便调用工具类读取properties文件的属性
    public static int getInteger(String key) {
        String value = props.getProperty(key.trim());
        if (Strings.isNullOrEmpty(value)) {
            throw new RuntimeException("没有配置属性：" + key);
        }
        return Integer.parseInt(value.trim());
    }

    public static int getInteger(String key, int defaultValue) {
        String value = props.getProperty(key.trim());
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value.trim());
    }

    public static String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key.trim());
        if (value.isEmpty()) {
            value = defaultValue;
        }
        return value.trim();
    }

}
