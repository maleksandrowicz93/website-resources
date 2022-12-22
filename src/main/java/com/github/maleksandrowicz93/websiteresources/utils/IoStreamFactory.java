package com.github.maleksandrowicz93.websiteresources.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public interface IoStreamFactory<S> {

    InputStream inputStream(S source) throws IOException;
    InputStreamReader inputStreamReader(InputStream inputStream);
}
