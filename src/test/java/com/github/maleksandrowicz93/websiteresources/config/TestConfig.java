package com.github.maleksandrowicz93.websiteresources.config;

import com.github.maleksandrowicz93.websiteresources.utils.IoStreamFactory;
import com.github.maleksandrowicz93.websiteresources.utils.MockIoStreamFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public IoStreamFactory<String> ioStreamFactory() {
        return new MockIoStreamFactory();
    }
}
