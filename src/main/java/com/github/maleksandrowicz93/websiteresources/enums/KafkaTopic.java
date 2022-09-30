package com.github.maleksandrowicz93.websiteresources.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum represents Kafka topics.
 */
@Getter
@RequiredArgsConstructor
public enum KafkaTopic {

    WEBSITE_DOWNLOAD("website-download"),
    NOTIFICATION("notification");

    private final String text;
}
