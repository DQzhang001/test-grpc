package com.convertlab.testNG;

import com.atf.cap.plugin.SystemPlugin;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestNGConfig {
    private static final Map<String, String> properties = new HashMap<>();

    static {
        properties.putIfAbsent("host","127.0.0.1");
        properties.putIfAbsent("port","9898");
    }
    public String getProperty(String key) {
        String value = SystemPlugin.getProperty(key);
        return value == null ? properties.get(key) : value;
    }

    public String setProperty(String key, String value) {
        return SystemPlugin.setProperty(key, value);
    }
}
