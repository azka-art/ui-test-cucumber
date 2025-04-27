package com.automation.tests.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();
    private static ConfigManager instance;

    private ConfigManager() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration loaded successfully");
                
                // Override with environment variables if present
                for (Object key : properties.keySet()) {
                    String envKey = ((String) key).replace('.', '_').toUpperCase();
                    String envValue = System.getenv(envKey);
                    if (envValue != null && !envValue.isEmpty()) {
                        properties.setProperty((String) key, envValue);
                        logger.info("Overriding '{}' with environment variable", key);
                    }
                }
                
                // Apply environment-specific overrides
                String environment = getProperty("env", "test");
                String envBaseUrl = getProperty(environment + ".baseUrl", null);
                if (envBaseUrl != null) {
                    properties.setProperty("baseUrl", envBaseUrl);
                    logger.info("Using '{}' environment baseUrl: {}", environment, envBaseUrl);
                }
            } else {
                logger.error("Configuration file not found!");
                throw new RuntimeException("config.properties not found in the classpath!");
            }
        } catch (IOException e) {
            logger.error("Error loading configuration: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        // First check system properties
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        // Then check our properties file
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse {} as integer, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
}
