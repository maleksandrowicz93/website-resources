package com.github.maleksandrowicz93.websiteresources.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public ISet<String> urlCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getSet("urls");
    }
}
