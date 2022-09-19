package com.github.maleksandrowicz93.websiteresources.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    WEBSITE_DOWNLOADING_STARTED("WEBSITE_DOWNLOADING_STARTED", "Website downloading started"),
    WEBSITE_DELETED("WEBSITE_DELETED", "Website deleted successfully");

    private final String code;
    private final String message;
}
