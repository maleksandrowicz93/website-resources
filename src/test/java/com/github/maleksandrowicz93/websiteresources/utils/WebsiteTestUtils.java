package com.github.maleksandrowicz93.websiteresources.utils;

import com.github.maleksandrowicz93.websiteresources.model.Website;

/**
 * This class contains constants and methods providing {@link Website} related data.
 */
public class WebsiteTestUtils {

    public static final String URL = "http://test.com";
    public static final long ID = 1;

    private WebsiteTestUtils() {}

    /**
     * This method provides {@link Website} instance for tests purposes.
     * @return {@link Website} instance
     */
    public static Website buildWebsite() {
        return Website.builder()
                .id(ID)
                .url(URL)
                .html("<html>")
                .build();
    }
}
