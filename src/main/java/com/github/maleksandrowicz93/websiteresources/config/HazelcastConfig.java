package com.github.maleksandrowicz93.websiteresources.config;

import com.github.maleksandrowicz93.websiteresources.enums.Caches;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
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
    public Set<String> temporaryUrlCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getSet(Caches.URLS.getText());
    }

    @Bean
    public Map<String, String> websiteCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap(Caches.WEBSITES.getText());
    }
}
