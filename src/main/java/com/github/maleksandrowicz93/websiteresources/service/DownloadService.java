package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.cache.UrlCache;
import com.github.maleksandrowicz93.websiteresources.entity.Website;
import com.github.maleksandrowicz93.websiteresources.exception.MalformedUrlException;
import com.github.maleksandrowicz93.websiteresources.repository.WebsiteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class DownloadService {

    private final WebsiteRepository websiteRepository;
    private final UrlCache urlCache;

    @Async
    public void downloadWebsite(String url) throws MalformedUrlException {
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
            throw new MalformedUrlException();
        } finally {
            urlCache.delete(url);
        }
    }
}
