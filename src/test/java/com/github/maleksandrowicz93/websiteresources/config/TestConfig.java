package com.github.maleksandrowicz93.websiteresources.config;

import com.github.maleksandrowicz93.websiteresources.utils.IOStreamFactory;
import com.github.maleksandrowicz93.websiteresources.utils.MockIOStreamFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Primary
@TestConfiguration
public class TestConfig {

    @Bean
    public IOStreamFactory<String> ioStreamFactory() {
        return new MockIOStreamFactory();
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
