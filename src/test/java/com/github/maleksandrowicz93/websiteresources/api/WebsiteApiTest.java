package com.github.maleksandrowicz93.websiteresources.api;

import com.github.maleksandrowicz93.websiteresources.annotation.IntegrationTest;
import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;
import com.github.maleksandrowicz93.websiteresources.enums.ResponseMessage;
import com.github.maleksandrowicz93.websiteresources.model.Website;
import com.github.maleksandrowicz93.websiteresources.repository.generic.WebsiteRepository;
import com.github.maleksandrowicz93.websiteresources.utils.ApiUtils;
import com.github.maleksandrowicz93.websiteresources.utils.WebsiteTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WithMockUser
public class WebsiteApiTest {

    private static final String URL = WebsiteTestUtils.URL;
    private static final String HTML = WebsiteTestUtils.HTML;
    private static final String BASE_PATH = ApiUtils.BASE_PATH;
    private static final String JSON_ROOT = "$";
    private static final String JSON_FIRST_ELEMENT = JSON_ROOT + "[0]";
    private static final int UUID_LENGTH = UUID.randomUUID().toString().length();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebsiteRepository websiteRepository;
    @Autowired
    private Set<String> temporaryUrlCache;
    @Autowired
    private Map<String, String> websiteCache;

    @AfterEach
    void cleanup() {
        websiteRepository.deleteAll();
        temporaryUrlCache.forEach(temporaryUrlCache::remove);
        websiteCache.forEach(websiteCache::remove);
    }

    @Test
    @DisplayName("Should get empty list when no website added")
    void shouldGetEmptyListWhenNoWebsiteAdded() throws Exception {
        mockMvc.perform(get(BASE_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT).isArray())
                .andExpect(jsonPath(JSON_ROOT).isEmpty());
    }

    @Test
    @DisplayName("Should get all websites")
    void shouldGetAllWebsites() throws Exception {
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
    @DisplayName("Should download website")
    void shouldDownloadWebsite() throws Exception {
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
    @DisplayName("Should not download website when is being downloaded")
    void shouldNotDownloadWebsiteWhenIsBeingDownloaded() throws Exception {
        ErrorCode errorCode = ErrorCode.WEBSITE_ALREADY_EXISTS;
        websiteCache.put(URL, HTML);
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
    @DisplayName("Should not download webiste when is already downloaded")
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
    @DisplayName("Should not donwload website when malformed URL")
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
    @DisplayName("Should get website")
    void shouldGetWebsite() throws Exception {
        Website website = WebsiteTestUtils.websiteToAdd();
        website = websiteRepository.save(website);
        mockMvc.perform(get(ApiUtils.getSpecifiedPath(website.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT).isNotEmpty())
                .andExpect(jsonPath(JSON_ROOT).isString())
                .andExpect(jsonPath(JSON_ROOT).value(HTML));
    }

    @Test
    @DisplayName("Should not get website if not exists")
    void shouldNotGetWebsiteIfNotExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(ApiUtils.getSpecifiedPath()))
                .andDo(print())
                .andExpect(status().isNotFound());
        testErrorResponse(ErrorCode.WEBSITE_NOT_FOUND, resultActions);
    }

    @Test
    @DisplayName("Should delete website")
    void shouldDeleteWebsite() throws Exception {
        Website website = WebsiteTestUtils.websiteToAdd();
        website = websiteRepository.save(website);
        ResponseMessage responseMessage = ResponseMessage.WEBSITE_DELETED;
        mockMvc.perform(delete(ApiUtils.getSpecifiedPath(website.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT).isNotEmpty())
                .andExpect(jsonPath(JSON_ROOT + ".code").value(responseMessage.getCode()))
                .andExpect(jsonPath(JSON_ROOT + ".message").value(responseMessage.getMessage()));
    }

    @Test
    @DisplayName("Should not delete website if not exists")
    void shouldNotDeleteWebsiteIfNotExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete(ApiUtils.getSpecifiedPath()))
                .andDo(print())
                .andExpect(status().isNotFound());
        testErrorResponse(ErrorCode.WEBSITE_NOT_FOUND, resultActions);
    }
}
