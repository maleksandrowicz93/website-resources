package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.enums.KafkaTopic;
import com.github.maleksandrowicz93.websiteresources.exception.InvalidUrlException;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteAlreadyExistsException;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteNotFoundException;
import com.github.maleksandrowicz93.websiteresources.model.Website;
import com.github.maleksandrowicz93.websiteresources.repository.jpa.JpaWebsiteRepository;
import com.github.maleksandrowicz93.websiteresources.utils.WebsiteTestUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This class tests {@link WebsiteService} public methods.
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class WebsiteServiceTest {

    private static final String URL = WebsiteTestUtils.URL;
    private static final String ID = WebsiteTestUtils.ID;

    private final MockedStatic<UrlValidator> urlValidatorMockedStatic = mockStatic(UrlValidator.class);

    @MockBean
    private DownloadService downloadService;
    @MockBean
    private JpaWebsiteRepository websiteRepository;
    @MockBean
    private Set<String> urlCache;
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    private WebsiteService websiteService;

    @BeforeEach
    void setup() {
        websiteService = new WebsiteService(downloadService, websiteRepository, urlCache, kafkaTemplate);
    }

    @AfterEach
    void cleanup() {
        urlValidatorMockedStatic.close();
    }

    @Test
    @DisplayName("Should download website successfully")
    void shouldDownloadWebsite() throws InvalidUrlException, WebsiteAlreadyExistsException {
        //given
        mockCheckingUrlBehavior();
        when(urlCache.contains(anyString())).thenReturn(false);
        when(websiteRepository.existsByUrl(anyString())).thenReturn(false);
        doNothing().when(downloadService).downloadWebsite(anyString());

        //when
        websiteService.downloadWebsite(URL);

        //then
        verify(kafkaTemplate).send(KafkaTopic.WEBSITE_DOWNLOAD.getText(), URL);
    }

    private void mockCheckingUrlBehavior() {
        UrlValidator urlValidator = mock(UrlValidator.class);
        urlValidatorMockedStatic.when(UrlValidator::getInstance).thenReturn(urlValidator);
        when(urlValidator.isValid(anyString())).thenReturn(true);
    }

    @Test
    @DisplayName("Should not download website when invalid URL")
    void shouldNotDownloadWebsiteWhenInvalidUrl() {
        //given
        UrlValidator urlValidator = mock(UrlValidator.class);
        urlValidatorMockedStatic.when(UrlValidator::getInstance).thenReturn(urlValidator);
        when(urlValidator.isValid(anyString())).thenReturn(false);

        //when
        assertThrows(InvalidUrlException.class, () -> websiteService.downloadWebsite(URL));

        //then
        verify(urlCache, times(0)).contains(URL);
        verify(websiteRepository, times(0)).existsByUrl(URL);
        verify(downloadService, times(0)).downloadWebsite(URL);
    }

    @Test
    @DisplayName("Should not download website when cache contains URL")
    void shouldNotDownloadWebsiteWhenCacheContainsUrl() {
        //given
        mockCheckingUrlBehavior();
        when(urlCache.contains(anyString())).thenReturn(true);

        //when
        assertThrows(WebsiteAlreadyExistsException.class, () -> websiteService.downloadWebsite(URL));

        //then
        verify(urlCache).contains(URL);
        verify(websiteRepository, times(0)).existsByUrl(URL);
        verify(downloadService, times(0)).downloadWebsite(URL);
    }

    @Test
    @DisplayName("Should not download website when repo contains URL")
    void shouldNotDownloadWebsiteWhenRepoContainsUrl() {
        //given
        mockCheckingUrlBehavior();
        when(urlCache.contains(anyString())).thenReturn(false);
        when(websiteRepository.existsByUrl(anyString())).thenReturn(true);

        //when
        assertThrows(WebsiteAlreadyExistsException.class, () -> websiteService.downloadWebsite(URL));

        //then
        verify(urlCache).contains(URL);
        verify(websiteRepository).existsByUrl(URL);
        verify(downloadService, times(0)).downloadWebsite(URL);
    }

    @Test
    @DisplayName("Should get all websites")
    void shouldGetAllWebsites() {
        //given
        Website website = WebsiteTestUtils.savedWebsite();
        List<Website> expectedWebsites = Collections.singletonList(website);
        when(websiteRepository.findAll()).thenReturn(expectedWebsites);

        //when
        List<Website> actualWebsites = websiteService.getAllWebsites();

        //then
        assertNotNull(actualWebsites);
        assertEquals(expectedWebsites, actualWebsites);
    }

    @Test
    @DisplayName("Should get website")
    void shouldGetWebsite() throws WebsiteNotFoundException {
        //given
        Website website = WebsiteTestUtils.savedWebsite();
        when(websiteRepository.findById(anyString())).thenReturn(Optional.of(website));

        //when
        String html = websiteService.getWebsite(ID);

        //then
        assertNotNull(html);
        assertEquals(website.getHtml(), html);
    }

    @Test
    @DisplayName("Should not get website when not stored in repo")
    void shouldNotGetWebsiteWhenNotStoredInRepo() {
        //given
        when(websiteRepository.findById(anyString())).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(WebsiteNotFoundException.class, () -> websiteService.getWebsite(ID));
    }

    @Test
    @DisplayName("Should delete website")
    void shouldDeleteWebsite() throws WebsiteNotFoundException {
        //given
        when(websiteRepository.existsById(anyString())).thenReturn(true);
        doNothing().when(websiteRepository).deleteById(anyString());

        //when
        websiteService.deleteWebsite(ID);

        //then
        verify(websiteRepository).deleteById(ID);
    }

    @Test
    @DisplayName("Should not delete website when not stored in repo")
    void shouldNotDeleteWebsiteWhenNotStoredInRepo() {
        //given
        when(websiteRepository.existsById(anyString())).thenReturn(false);

        //when
        assertThrows(WebsiteNotFoundException.class, () -> websiteService.deleteWebsite(ID));

        //then
        verify(websiteRepository, times(0)).deleteById(ID);
    }
}
