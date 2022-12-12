package com.github.maleksandrowicz93.websiteresources.api;

import com.github.maleksandrowicz93.websiteresources.converters.WebsiteToWebsiteDtoConverter;
import com.github.maleksandrowicz93.websiteresources.dto.ResponseDto;
import com.github.maleksandrowicz93.websiteresources.dto.WebsiteDto;
import com.github.maleksandrowicz93.websiteresources.model.Website;
import com.github.maleksandrowicz93.websiteresources.enums.ResponseMessage;
import com.github.maleksandrowicz93.websiteresources.exception.InvalidUrlException;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteAlreadyExistsException;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteNotFoundException;
import com.github.maleksandrowicz93.websiteresources.service.WebsiteService;
import com.github.maleksandrowicz93.websiteresources.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements {@link WebsiteApi} contract,
 */
@RestController
@RequiredArgsConstructor
public class WebsiteControllerApiImpl implements WebsiteApi {

    private final WebsiteService websiteService;

    @Override
    @SneakyThrows({WebsiteAlreadyExistsException.class, InvalidUrlException.class})
    public ResponseEntity<ResponseDto> downloadWebsite(@RequestBody String url) {
        websiteService.downloadWebsite(url);
        return ResponseFactory.response(ResponseMessage.WEBSITE_DOWNLOADING_STARTED);
    }

    @Override
    public ResponseEntity<List<WebsiteDto>> getAllWebsites() {
        List<Website> websites = websiteService.getAllWebsites();
        List<WebsiteDto> websiteDtoList = websites.stream()
                .map(WebsiteToWebsiteDtoConverter.INSTANCE::convert)
                .collect(Collectors.toList());
        return ResponseEntity.ok(websiteDtoList);
    }

    @Override
    @SneakyThrows(WebsiteNotFoundException.class)
    public ResponseEntity<String> getWebsite(@PathVariable String id) {
        String website = websiteService.getWebsite(id);
        return ResponseEntity.ok(website);
    }

    @Override
    @SneakyThrows(WebsiteNotFoundException.class)
    public ResponseEntity<ResponseDto> deleteWebsite(@PathVariable String id) {
        websiteService.deleteWebsite(id);
        return ResponseFactory.response(ResponseMessage.WEBSITE_DELETED);
    }
}
