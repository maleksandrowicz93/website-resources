package com.github.maleksandrowicz93.websiteresources.utils;

import com.github.maleksandrowicz93.websiteresources.model.Website;

/**
 * This class contains constants and methods providing {@link Website} related data.
 */
public class WebsiteTestUtils {

    public static final String URL = "http://test.com";
    public static final String ID = "1";
    public static final String HTML = "<html>";

    private WebsiteTestUtils() {}

    /**
     * This method provides {@link Website} instance with declared id for tests purposes.
     * @return {@link Website} instance
     */
    public static Website savedWebsite() {
        return Website.builder()
                .id(ID)
                .url(URL)
                .html(HTML)
                .build();
    }

    /**
     * This method provides {@link Website} instance with declared id for tests purposes.
     * @return {@link Website} instance
     */
    public static Website websiteToAdd() {
        return Website.builder()
                .url(URL)
                .html(HTML)
                .build();
    }
}
