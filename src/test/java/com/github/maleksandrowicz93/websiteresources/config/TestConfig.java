package com.github.maleksandrowicz93.websiteresources.config;

import com.github.maleksandrowicz93.websiteresources.utils.IoStreamFactory;
import com.github.maleksandrowicz93.websiteresources.utils.MockIoStreamFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class TestConfig {

    @Bean
    public IoStreamFactory<String> ioStreamFactory() {
        return new MockIoStreamFactory();
    }

    @Bean
    public Map<String, String> testUrlCache() {
        return new HashMap<>();
    }
}
