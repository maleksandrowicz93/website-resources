package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.annotation.EmbeddedKafkaTest;
import com.github.maleksandrowicz93.websiteresources.enums.KafkaTopic;
import com.github.maleksandrowicz93.websiteresources.model.Website;
import com.github.maleksandrowicz93.websiteresources.repository.jpa.JpaWebsiteRepository;
import com.github.maleksandrowicz93.websiteresources.utils.UrlIoStreamFactory;
import com.github.maleksandrowicz93.websiteresources.utils.WebsiteTestUtils;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * This class tests {@link DownloadService} public methods.
 */
@EmbeddedKafkaTest
class DownloadServiceTest {

    private static final String URL = WebsiteTestUtils.URL;
    private static final String ID = WebsiteTestUtils.ID;

    private final MockedStatic<IOUtils> ioUtilsMockedStatic = mockStatic(IOUtils.class);

    @MockBean
    private JpaWebsiteRepository websiteRepository;
    @MockBean
    private Set<String> urlCache;
    @MockBean
    private UrlIoStreamFactory ioStreamFactory;
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;
    @SpyBean
    private Gson gson;

    private DownloadService downloadService;

    @BeforeEach
    void setup() {
        downloadService = new DownloadService(websiteRepository, urlCache, ioStreamFactory, kafkaTemplate, gson);
    }

    @AfterEach
    void cleanup() {
        ioUtilsMockedStatic.close();
    }

    @Test
    @DisplayName("Should download website")
    void shouldDownloadWebsite() throws IOException {
        //given
        Website expectedWebsite = WebsiteTestUtils.savedWebsite();
        when(urlCache.add(anyString())).thenReturn(true);
        InputStream inputStream = mock(InputStream.class);
        when(ioStreamFactory.inputStream(anyString())).thenReturn(inputStream);
        InputStreamReader inputStreamReader = mock(InputStreamReader.class);
        when(ioStreamFactory.inputStreamReader(inputStream)).thenReturn(inputStreamReader);
        String html = expectedWebsite.getHtml();
        ioUtilsMockedStatic.when(() -> IOUtils.toString(inputStreamReader)).thenReturn(html);
        ArgumentCaptor<Website> websiteArgumentCaptor = ArgumentCaptor.forClass(Website.class);
        when(websiteRepository.save(websiteArgumentCaptor.capture())).thenAnswer(answer -> {
            Object argument = answer.getArgument(0);
            assertTrue(argument instanceof Website);
            Website website = (Website) argument;
            website.setId(ID);
            return website;
        });
        when(urlCache.remove(anyString())).thenReturn(true);

        //when
        downloadService.onDownloadWebsite(URL);

        //then
        verify(urlCache).add(URL);
        verify(websiteRepository).save(any(Website.class));
        verify(kafkaTemplate).send(eq(KafkaTopic.NOTIFICATION.getText()), anyString());
        verify(urlCache).remove(URL);

        assertEquals(expectedWebsite, websiteArgumentCaptor.getValue());
    }

    @Test
    @DisplayName("Should download website when malformed URL")
    void shouldNotDownloadWebsiteWhenMalformedUrl() throws IOException {
        //given
        when(urlCache.add(anyString())).thenReturn(true);
        when(ioStreamFactory.inputStream(anyString())).thenThrow(MalformedURLException.class);
        when(urlCache.remove(anyString())).thenReturn(true);

        //when
        downloadService.onDownloadWebsite(URL);

        //then
        verify(urlCache).add(URL);
        verify(websiteRepository, times(0)).save(any(Website.class));
        verify(kafkaTemplate).send(eq(KafkaTopic.NOTIFICATION.getText()), anyString());
        verify(urlCache).remove(URL);
    }
}
