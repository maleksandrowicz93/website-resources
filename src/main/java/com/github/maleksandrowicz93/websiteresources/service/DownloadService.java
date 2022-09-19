package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.cache.UrlCache;
import com.github.maleksandrowicz93.websiteresources.entity.Website;
import com.github.maleksandrowicz93.websiteresources.repository.WebsiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This class serves downloading operations.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DownloadService {

    private final WebsiteRepository websiteRepository;
    private final UrlCache urlCache;

    /**
     * This method asynchronously download website from given url.
     * @param url - String url of website to be downloaded
     */
    @Async
    public void downloadWebsite(String url) {
        urlCache.put(url);
        try (InputStream inputStream = new URL(url).openStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            String html = IOUtils.toString(inputStreamReader);
            Website website = Website.builder()
                    .url(url)
                    .html(html)
                    .build();
            websiteRepository.save(website);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            urlCache.delete(url);
        }
    }
}
