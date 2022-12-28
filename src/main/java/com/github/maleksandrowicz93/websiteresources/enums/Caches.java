package com.github.maleksandrowicz93.websiteresources.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Caches {

    URLS("urls"),
    WEBSITES("websites");

    private final String text;
}
