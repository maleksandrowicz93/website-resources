package com.github.maleksandrowicz93.websiteresources.api;

import com.github.maleksandrowicz93.websiteresources.annotation.EmbeddedKafkaTest;
import com.github.maleksandrowicz93.websiteresources.config.Profiles;
import com.github.maleksandrowicz93.websiteresources.config.TestConfig;
import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;
import com.github.maleksandrowicz93.websiteresources.enums.ResponseMessage;
import com.github.maleksandrowicz93.websiteresources.model.Website;
import com.github.maleksandrowicz93.websiteresources.repository.generic.WebsiteRepository;
import com.github.maleksandrowicz93.websiteresources.utils.WebsiteTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EmbeddedKafkaTest
@Import(TestConfig.class)
@ActiveProfiles(profiles = Profiles.DEV)
@AutoConfigureMockMvc
public class WebsiteApiTest {

    private static final String URL = WebsiteTestUtils.URL;
    private static final String HTML = WebsiteTestUtils.HTML;
    private static final String BASE_PATH = "/website";
    private static final String JSON_ROOT = "$";
    private static final String JSON_FIRST_ELEMENT = JSON_ROOT + "[0]";
    private static final int UUID_LENGTH = UUID.randomUUID().toString().length();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebsiteApi websiteApi;
    @Autowired
    private WebsiteRepository websiteRepository;
    @Autowired
    private Set<String> urlCache;

    @BeforeEach
    void setup() {
        websiteRepository.deleteAll();
        urlCache.forEach(urlCache::remove);
    }

    @Test
    void getEmptyListWhenNoWebsiteAdded() throws Exception {
        mockMvc.perform(get(BASE_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT).isArray())
                .andExpect(jsonPath(JSON_ROOT).isEmpty());
    }

    @Test
    void getAllWebsites() throws Exception {
        Website website = websiteRepository.save(WebsiteTestUtils.websiteToAdd());
        mockMvc.perform(get(BASE_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT).isNotEmpty())
                .andExpect(jsonPath(JSON_ROOT).isArray())
                .andExpect(jsonPath(JSON_ROOT, hasSize(1)))
                .andExpect(jsonPath(JSON_FIRST_ELEMENT).exists())
                .andExpect(jsonPath(JSON_FIRST_ELEMENT + ".id").value(website.getId()))
                .andExpect(jsonPath(JSON_FIRST_ELEMENT + ".url").value(website.getUrl()));
    }

    @Test
    void downloadWebsite() throws Exception {
        ResponseMessage responseMessage = ResponseMessage.WEBSITE_DOWNLOADING_STARTED;
        mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT).isNotEmpty())
                .andExpect(jsonPath(JSON_ROOT + ".code").value(responseMessage.getCode()))
                .andExpect(jsonPath(JSON_ROOT + ".message").value(responseMessage.getMessage()));
    }

    @Test
    void shouldNotDownloadWebsiteWhenIsBeingDownloaded() throws Exception {
        ErrorCode errorCode = ErrorCode.WEBSITE_ALREADY_EXISTS;
        urlCache.add(URL);
        ResultActions resultActions = mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(URL))
                .andDo(print())
                .andExpect(status().isBadRequest());
        testErrorResponse(errorCode, resultActions);
    }

    private void testErrorResponse(ErrorCode errorCode, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(jsonPath(JSON_ROOT).isNotEmpty())
                .andExpect(jsonPath(JSON_ROOT + ".code").value(errorCode.getCode()))
                .andExpect(jsonPath(JSON_ROOT + ".message").value(errorCode.getMessage()))
                .andExpect(jsonPath(JSON_ROOT + ".uuid").isNotEmpty())
                .andExpect(jsonPath(JSON_ROOT + ".uuid").isString())
                .andExpect(jsonPath(JSON_ROOT + ".uuid", hasLength(UUID_LENGTH)));
    }

    @Test
    void shouldNotDownloadWebsiteWhenIsAlreadyDownloaded() throws Exception {
        ErrorCode errorCode = ErrorCode.WEBSITE_ALREADY_EXISTS;
        websiteRepository.save(WebsiteTestUtils.websiteToAdd());
        ResultActions resultActions = mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(URL))
                .andDo(print())
                .andExpect(status().isBadRequest());
        testErrorResponse(errorCode, resultActions);
    }

    @Test
    void shouldNotDownloadWebsiteWhenMalformedUrl() throws Exception {
        ErrorCode errorCode = ErrorCode.INVALID_URL;
        ResultActions resultActions = mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("malformed-url"))
                .andDo(print())
                .andExpect(status().isBadRequest());
        testErrorResponse(errorCode, resultActions);
    }

    @Test
    void getWebsite() throws Exception {
        Website website = WebsiteTestUtils.websiteToAdd();
        website = websiteRepository.save(website);
        mockMvc.perform(get(getSpecifiedPath(website.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT).isNotEmpty())
                .andExpect(jsonPath(JSON_ROOT).isString())
                .andExpect(jsonPath(JSON_ROOT).value(HTML));
    }

    private String getSpecifiedPath(String id) {
        return BASE_PATH + "/" + id;
    }

    @Test
    void shouldNotGetWebsiteIfNotExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(getSpecifiedPath("1")))
                .andDo(print())
                .andExpect(status().isNotFound());
        testErrorResponse(ErrorCode.WEBSITE_NOT_FOUND, resultActions);
    }

    @Test
    void deleteWebsite() throws Exception {
        Website website = WebsiteTestUtils.websiteToAdd();
        website = websiteRepository.save(website);
        ResponseMessage responseMessage = ResponseMessage.WEBSITE_DELETED;
        mockMvc.perform(delete(getSpecifiedPath(website.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT).isNotEmpty())
                .andExpect(jsonPath(JSON_ROOT + ".code").value(responseMessage.getCode()))
                .andExpect(jsonPath(JSON_ROOT + ".message").value(responseMessage.getMessage()));
    }

    @Test
    void shouldNotDeleteWebsiteIfNotExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete(getSpecifiedPath("1")))
                .andDo(print())
                .andExpect(status().isNotFound());
        testErrorResponse(ErrorCode.WEBSITE_NOT_FOUND, resultActions);
    }
}
