package com.github.maleksandrowicz93.websiteresources.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MockIOStreamFactory implements IOStreamFactory<String> {

    @Override
    public InputStream inputStream(String url) throws IOException {
        return new ByteArrayInputStream(url.getBytes());
    }

    @Override
    public InputStreamReader inputStreamReader(InputStream inputStream) {
        return new InputStreamReader(inputStream);
    }
}
