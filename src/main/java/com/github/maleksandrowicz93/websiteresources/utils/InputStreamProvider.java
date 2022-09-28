package com.github.maleksandrowicz93.websiteresources.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This class provides {@link InputStream} instances.
 */
public class InputStreamProvider {

    private InputStreamProvider() {}

    /**
     * This method provides {@link InputStream} instance from String url.
     * @param url - String url
     * @return {@link InputStream} instance
     * @throws IOException when malformed URL
     */
    public static InputStream from(String url) throws IOException {
        return new URL(url).openStream();
    }
}
