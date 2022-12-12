package com.github.maleksandrowicz93.websiteresources.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Profile({Profiles.DEV, Profiles.MONGO})
@Configuration
public class BasicConfig {

    @Bean
    public Set<String> urlCache() {
        return ConcurrentHashMap.newKeySet();
    }
}
