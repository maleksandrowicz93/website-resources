package com.github.maleksandrowicz93.websiteresources.api;

import com.github.maleksandrowicz93.websiteresources.annotation.IntegrationTest;
import com.github.maleksandrowicz93.websiteresources.utils.ApiUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class SecurityTest {

    private static final String BASE_PATH = ApiUtils.BASE_PATH;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should not get all websites if not authenticated")
    void shouldNotGetAllWebsitesIfNotAuthenticated() throws Exception {
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should not download website if not authenticated")
    void shouldNotDownloadWebsiteIfNotAuthenticated() throws Exception {
        mockMvc.perform(post(BASE_PATH))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should not get website if not authenticated")
    void shouldNotGetWebsiteIfNotAuthenticated() throws Exception {
        mockMvc.perform(get(ApiUtils.getSpecifiedPath()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should not delete website if not authenticated")
    void shouldNotDeleteWebsiteIfNotAuthenticated() throws Exception {
        mockMvc.perform(delete(ApiUtils.getSpecifiedPath()))
                .andExpect(status().isUnauthorized());
    }
}
