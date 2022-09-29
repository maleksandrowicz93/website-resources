package com.github.maleksandrowicz93.websiteresources.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopic {

    WEBSITE_DOWNLOAD("website-download"),
    NOTIFICATION("notification");

    private final String text;
}
