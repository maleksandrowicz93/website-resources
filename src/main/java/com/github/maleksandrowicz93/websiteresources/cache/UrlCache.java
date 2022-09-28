package com.github.maleksandrowicz93.websiteresources.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents cache for url of downloaded websites.
 * Cache stores urls during downloading time.
 */
@Component
@RequiredArgsConstructor
public class UrlCache {

    private static final Set<String> urlCache = new HashSet<>();

    /**
     * This method check if cache contains a given url.
     * @param url - String url
     * @return boolean checking result
     */
    public boolean contains(String url) {
        return urlCache.contains(url);
    }

    /**
     * This method puts url into cache.
     * @param url - String url to be put
     */
    public void put(String url) {
        urlCache.add(url);
    }

    /**
     * This method deletes url from cache.
     * @param url -String url to be deleted
     */
    public void delete(String url) {
        urlCache.remove(url);
    }
}
