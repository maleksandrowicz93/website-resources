package com.github.maleksandrowicz93.websiteresources.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;
import java.util.Set;


/**
 * This class stores configuration of Hazelcast.
 */
@Primary
@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        return HazelcastClient.newHazelcastClient();
    }

    @Bean
    public Set<String> temporaryUrlCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getSet(Caches.URLS);
    }

    @Bean
    public Map<String, String> websiteCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap(Caches.WEBSITES);
    }
}
