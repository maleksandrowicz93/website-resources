package com.github.maleksandrowicz93.websiteresources.config;

import com.github.maleksandrowicz93.websiteresources.utils.IoStreamFactory;
import com.github.maleksandrowicz93.websiteresources.utils.MockIoStreamFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@TestConfiguration
public class TestConfig {

    @Bean
    public IoStreamFactory<String> ioStreamFactory() {
        return new MockIoStreamFactory();
    }

    @Bean("temporaryUrlCacheForTests")
    public Set<String> temporaryUrlCache() {
        return new HashSet<>();
    }

    @Bean("websiteCacheForTests")
    public Map<String, String> websiteCache() {
        return new HashMap<>();
    }
}
