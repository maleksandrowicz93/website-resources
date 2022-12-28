package com.github.maleksandrowicz93.websiteresources.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum represents Kafka topics.
 */
public interface KafkaTopic {

    String DOWNLOAD_WEBSITE = "download-website";
    String NOTIFICATION = "notification";
}
