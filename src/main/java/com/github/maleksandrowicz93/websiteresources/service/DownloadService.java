package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.cache.UrlCache;
import com.github.maleksandrowicz93.websiteresources.entity.Website;
import com.github.maleksandrowicz93.websiteresources.enums.KafkaTopic;
import com.github.maleksandrowicz93.websiteresources.repository.WebsiteRepository;
import com.github.maleksandrowicz93.websiteresources.utils.InputStreamProvider;
import com.github.maleksandrowicz93.websiteresources.utils.InputStreamReaderProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class serves downloading operations.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DownloadService {

    private final WebsiteRepository websiteRepository;
    private final UrlCache urlCache;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * This method asynchronously download website from given url.
     * @param url - String url of website to be downloaded
     */
    @KafkaListener(topics = "website-download", groupId = "website-resources")
    public void downloadWebsite(String url) {
        log.info("Retrieved download website job for url: {}", url);
        log.info("Putting url into temporary cache");
        urlCache.put(url);
        log.info("Getting html code from url");
        String topic = KafkaTopic.NOTIFICATION.getText();
        try (InputStream inputStream = InputStreamProvider.from(url)) {
            InputStreamReader inputStreamReader = InputStreamReaderProvider.from(inputStream);
            String html = IOUtils.toString(inputStreamReader);
            Website website = Website.builder()
                    .url(url)
                    .html(html)
                    .build();
            log.info("Saving website into database");
            websiteRepository.save(website);
            kafkaTemplate.send(topic, "Website downloaded successfully");
        } catch (IOException e) {
            log.error(e.getMessage());
            kafkaTemplate.send(topic, "Website downloading failed");
        } finally {
            log.info("Deleting url from temporary cache");
            urlCache.delete(url);
        }
    }
}
