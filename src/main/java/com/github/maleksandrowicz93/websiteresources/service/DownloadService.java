package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.cache.UrlCache;
import com.github.maleksandrowicz93.websiteresources.entity.Website;
import com.github.maleksandrowicz93.websiteresources.repository.WebsiteRepository;
import com.github.maleksandrowicz93.websiteresources.utils.InputStreamProvider;
import com.github.maleksandrowicz93.websiteresources.utils.InputStreamReaderProvider;
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
        log.info("Download website job started for url: {}", url);
        log.info("Putting url into temporary cache");
        urlCache.put(url);
        log.info("Getting html code from url");
        try (InputStream inputStream = InputStreamProvider.from(url)) {
            InputStreamReader inputStreamReader = InputStreamReaderProvider.from(inputStream);
            String html = IOUtils.toString(inputStreamReader);
            Website website = Website.builder()
                    .url(url)
                    .html(html)
                    .build();
            log.info("Saving website into database");
            websiteRepository.save(website);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            log.info("Deleting url from temporary cache");
            urlCache.delete(url);
        }
    }
}
