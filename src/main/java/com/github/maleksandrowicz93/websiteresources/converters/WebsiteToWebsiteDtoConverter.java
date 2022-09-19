package com.github.maleksandrowicz93.websiteresources.converters;

import com.github.maleksandrowicz93.websiteresources.dto.WebsiteDto;
import com.github.maleksandrowicz93.websiteresources.entity.Website;

public enum WebsiteToWebsiteDtoConverter implements Converter<Website, WebsiteDto> {

    INSTANCE;

    @Override
    public WebsiteDto convert(Website website) {
        return WebsiteDto.builder()
                .id(website.getId())
                .url(website.getUrl())
                .build();
    }
}
