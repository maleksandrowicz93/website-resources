package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.cache.UrlCache;
import com.github.maleksandrowicz93.websiteresources.entity.Website;
import com.github.maleksandrowicz93.websiteresources.enums.KafkaTopic;
import com.github.maleksandrowicz93.websiteresources.repository.WebsiteRepository;
import com.github.maleksandrowicz93.websiteresources.utils.InputStreamProvider;
import com.github.maleksandrowicz93.websiteresources.utils.InputStreamReaderProvider;
import com.github.maleksandrowicz93.websiteresources.utils.WebsiteTestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * This class tests {@link DownloadService} public methods.
 */
@SpringBootTest
class DownloadServiceTest {

    private static final String URL = WebsiteTestUtils.URL;
    private static final long ID = WebsiteTestUtils.ID;

    private final MockedStatic<InputStreamProvider> inputStreamProviderMockedStatic = mockStatic(InputStreamProvider.class);
    private final MockedStatic<InputStreamReaderProvider> inputStreamReaderProviderMockedStatic = mockStatic(InputStreamReaderProvider.class);
    private final MockedStatic<IOUtils> ioUtilsMockedStatic = mockStatic(IOUtils.class);

    @MockBean
    private WebsiteRepository websiteRepository;
    @MockBean
    private UrlCache urlCache;
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    private DownloadService downloadService;

    @BeforeEach
    void setup() {
        downloadService = new DownloadService(websiteRepository, urlCache, kafkaTemplate);
    }

    @AfterEach
    void cleanup() {
        inputStreamProviderMockedStatic.close();
        inputStreamReaderProviderMockedStatic.close();
        ioUtilsMockedStatic.close();
    }

    @Test
    @DisplayName("Should download website")
    void shouldDownloadWebsite() {
        //given
        Website expectedWebsite = WebsiteTestUtils.buildWebsite();
        doNothing().when(urlCache).put(anyString());
        InputStream inputStream = mock(InputStream.class);
        inputStreamProviderMockedStatic.when(() -> InputStreamProvider.from(anyString())).thenReturn(inputStream);
        InputStreamReader inputStreamReader = mock(InputStreamReader.class);
        inputStreamReaderProviderMockedStatic.when(() -> InputStreamReaderProvider.from(inputStream))
                .thenReturn(inputStreamReader);
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
        doNothing().when(urlCache).delete(anyString());

        //when
        downloadService.downloadWebsite(URL);

        //then
        verify(urlCache).put(URL);
        verify(websiteRepository).save(any(Website.class));
        verify(kafkaTemplate).send(eq(KafkaTopic.NOTIFICATION.getText()), anyString());
        verify(urlCache).delete(URL);

        assertEquals(expectedWebsite, websiteArgumentCaptor.getValue());
    }

    @Test
    @DisplayName("Should download website when malformed URL")
    void shouldNotDownloadWebsiteWhenMalformedUrl() {
        //given
        doNothing().when(urlCache).put(anyString());
        inputStreamProviderMockedStatic.when(() -> InputStreamProvider.from(anyString()))
                .thenThrow(MalformedURLException.class);
        doNothing().when(urlCache).delete(anyString());

        //when
        downloadService.downloadWebsite(URL);

        //then
        verify(urlCache).put(URL);
        verify(websiteRepository, times(0)).save(any(Website.class));
        verify(kafkaTemplate).send(eq(KafkaTopic.NOTIFICATION.getText()), anyString());
        verify(urlCache).delete(URL);
    }
}
