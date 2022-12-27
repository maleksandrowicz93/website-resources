package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.dto.ErrorResponseDto;
import com.github.maleksandrowicz93.websiteresources.dto.ResponseDto;
import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;
import com.github.maleksandrowicz93.websiteresources.enums.KafkaTopic;
import com.github.maleksandrowicz93.websiteresources.enums.ResponseMessage;
import com.github.maleksandrowicz93.websiteresources.model.Website;
import com.github.maleksandrowicz93.websiteresources.repository.generic.WebsiteRepository;
import com.github.maleksandrowicz93.websiteresources.utils.IoStreamFactory;
import com.github.maleksandrowicz93.websiteresources.utils.ResponseFactory;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;

/**
 * This class serves downloading operations.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DownloadService {

    private final WebsiteRepository websiteRepository;
    private final Map<String, String> urlCache;
    private final IoStreamFactory<String> ioStreamFactory;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    /**
     * This method asynchronously download website from given url.
     * @param url - String url of website to be downloaded
     */
    @KafkaListener(topics = "download-website", groupId = "website-resources")
    public void onDownloadWebsite(String url) {
        log.info("Retrieved download website job for url: {}", url);
        log.info("Putting url into cache");
        urlCache.putIfAbsent(url, null);
        log.info("Getting html code from url");
        String topic = KafkaTopic.NOTIFICATION.getText();
        try (InputStream inputStream = ioStreamFactory.inputStream(url)) {
            InputStreamReader inputStreamReader = ioStreamFactory.inputStreamReader(inputStream);
            String html = IOUtils.toString(inputStreamReader);
            log.info("Saving website into database");
            urlCache.replace(url, html);
            Website website = Website.builder()
                    .url(url)
                    .html(html)
                    .build();
            log.info("Saving website into cache");
            websiteRepository.save(website);
            ResponseMessage responseMessage = ResponseMessage.WEBSITE_DOWNLOADED_SUCCESSFULLY;
            ResponseDto responseDto = ResponseFactory.responseDto(responseMessage);
            String json = gson.toJson(responseDto);
            kafkaTemplate.send(topic, json);
        } catch (IOException e) {
            UUID uuid = UUID.randomUUID();
            log.error("Error with UUID {}: {}", uuid, e.getMessage());
            ErrorCode errorCode = ErrorCode.WEBSITE_DOWNLOADING_FAILED;
            ErrorResponseDto errorResponseDto = ResponseFactory.errorResponseDto(errorCode, uuid);
            String json = gson.toJson(errorResponseDto);
            kafkaTemplate.send(topic, json);
        }
    }
}
