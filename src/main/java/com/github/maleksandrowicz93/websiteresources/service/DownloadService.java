package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.dto.ErrorResponseDto;
import com.github.maleksandrowicz93.websiteresources.dto.ResponseDto;
import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;
import com.github.maleksandrowicz93.websiteresources.config.KafkaTopic;
import com.github.maleksandrowicz93.websiteresources.enums.ResponseMessage;
import com.github.maleksandrowicz93.websiteresources.model.Website;
import com.github.maleksandrowicz93.websiteresources.repository.generic.WebsiteRepository;
import com.github.maleksandrowicz93.websiteresources.utils.IOStreamFactory;
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
import java.util.Set;
import java.util.UUID;

/**
 * This class serves downloading operations.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DownloadService {

    private final WebsiteRepository websiteRepository;
    private final Set<String> temporaryUrlCache;
    private final Map<String, String> websiteCache;
    private final IOStreamFactory<String> ioStreamFactory;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    /**
     * This method asynchronously download website from given url.`r
     * @param url - String url of website to be downloaded
     */
    @KafkaListener(topics = KafkaTopic.DOWNLOAD_WEBSITE, groupId = "website-resources")
    public void onDownloadWebsite(String url) {
        log.info("Retrieved download website job for url: {}", url);
        log.info("Adding url: {} into temporary cache", url);
        temporaryUrlCache.add(url);
        log.info("Downloading html code from url");
        try (InputStream inputStream = ioStreamFactory.inputStream(url)) {
            InputStreamReader inputStreamReader = ioStreamFactory.inputStreamReader(inputStream);
            String html = IOUtils.toString(inputStreamReader);
            log.info("Html code downloaded");
            Website website = Website.builder()
                    .url(url)
                    .html(html)
                    .build();
            log.info("Saving website into database");
            websiteRepository.save(website);
            log.info("Adding html to websites' cache");
            websiteCache.put(website.getId(), html);
            ResponseMessage responseMessage = ResponseMessage.WEBSITE_DOWNLOADED_SUCCESSFULLY;
            ResponseDto responseDto = ResponseFactory.responseDto(responseMessage);
            String json = gson.toJson(responseDto);
            kafkaTemplate.send(KafkaTopic.NOTIFICATION, json);
        } catch (IOException e) {
            UUID uuid = UUID.randomUUID();
            log.error("Error with UUID {}: {}", uuid, e.getMessage());
            ErrorCode errorCode = ErrorCode.WEBSITE_DOWNLOADING_FAILED;
            ErrorResponseDto errorResponseDto = ResponseFactory.errorResponseDto(errorCode, uuid);
            String json = gson.toJson(errorResponseDto);
            kafkaTemplate.send(KafkaTopic.NOTIFICATION, json);
        } finally {
            log.info("Removing url: {} from temporary url cache", url);
            temporaryUrlCache.remove(url);
        }
    }
}
