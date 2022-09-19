package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.cache.UrlCache;
import com.github.maleksandrowicz93.websiteresources.entity.Website;
import com.github.maleksandrowicz93.websiteresources.exception.MalformedUrlException;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteAlreadyExistsException;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteNotFoundException;
import com.github.maleksandrowicz93.websiteresources.repository.WebsiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class WebsiteService {

    private final DownloadService downloadService;
    private final WebsiteRepository websiteRepository;
    private final UrlCache urlCache;

    public void downloadWebsite(String url) throws WebsiteAlreadyExistsException, MalformedUrlException {
        if (urlCache.isUrlSaved(url) || websiteRepository.existsByUrl(url)) {
            throw new WebsiteAlreadyExistsException();
        }
        downloadService.downloadWebsite(url);
    }

    public List<Website> getAllWebsites() {
        return websiteRepository.findAll();
    }

    public String getWebsite(long id) throws WebsiteNotFoundException {
        return websiteRepository.findById(id)
                .map(Website::getHtml)
                .orElseThrow(WebsiteNotFoundException::new);
    }

    public void deleteWebsite(long id) throws WebsiteNotFoundException {
        if (!websiteRepository.existsById(id)) {
            throw new WebsiteNotFoundException();
        }
        websiteRepository.deleteById(id);
    }
}
