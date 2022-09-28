package com.github.maleksandrowicz93.websiteresources.utils;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class provides {@link InputStreamReader} instances.
 */
public class InputStreamReaderProvider {

    private InputStreamReaderProvider() {}

    /**
     * This method provides {@link InputStreamReader} instance from {@link InputStream} instance.
     * @param inputStream - {@link InputStream} instance
     * @return {@link InputStreamReader} instance
     */
    public static InputStreamReader from(InputStream inputStream) {
        return new InputStreamReader(inputStream);
    }
}
