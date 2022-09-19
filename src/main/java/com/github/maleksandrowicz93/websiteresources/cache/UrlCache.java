package com.github.maleksandrowicz93.websiteresources.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UrlCache {

    private static final Set<String> urlCache = new HashSet<>();

    public boolean isUrlSaved(String url) {
        return urlCache.contains(url);
    }

    public void put(String url) {
        urlCache.add(url);
    }

    public void delete(String url) {
        urlCache.remove(url);
    }
}
