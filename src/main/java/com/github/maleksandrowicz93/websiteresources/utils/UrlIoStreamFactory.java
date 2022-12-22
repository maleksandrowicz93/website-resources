package com.github.maleksandrowicz93.websiteresources.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This class provides {@link InputStream} instances.
 */
@Component
public class UrlIoStreamFactory implements IoStreamFactory<String> {

    /**
     * This method provides {@link InputStream} instance from String url.
     * @param url - String url
     * @return {@link InputStream} instance
     * @throws IOException when malformed URL
     */
    @Override
    public InputStream inputStream(String url) throws IOException {
        return new URL(url).openStream();
    }

    /**
     * This method provides {@link InputStreamReader} instance from {@link InputStream} instance.
     * @param inputStream - {@link InputStream} instance
     * @return {@link InputStreamReader} instance
     */
    @Override
    public InputStreamReader inputStreamReader(InputStream inputStream) {
        return new InputStreamReader(inputStream);
    }
}
