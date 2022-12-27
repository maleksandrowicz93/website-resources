package com.github.maleksandrowicz93.websiteresources.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

/**
 * This class stores configuration of Hazelcast.
 */
@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        return HazelcastClient.newHazelcastClient();
    }

    @Bean
    public Set<String> urlCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getSet("urls");
    }
}
