package com.github.maleksandrowicz93.websiteresources.service;

import com.github.maleksandrowicz93.websiteresources.cache.UrlCache;
import com.github.maleksandrowicz93.websiteresources.entity.Website;
import com.github.maleksandrowicz93.websiteresources.exception.InvalidUrlException;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteAlreadyExistsException;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteNotFoundException;
import com.github.maleksandrowicz93.websiteresources.repository.WebsiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class contains business logic of {@link Website} management.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class WebsiteService {

    private final DownloadService downloadService;
    private final WebsiteRepository websiteRepository;
    private final UrlCache urlCache;

    /**
     * This method trigger downloading website.
     * @param url - String url of website to be downloaded
     * @throws WebsiteAlreadyExistsException when try to download already downloaded website
     */
    public void downloadWebsite(String url) throws WebsiteAlreadyExistsException, InvalidUrlException {
        UrlValidator urlValidator = UrlValidator.getInstance();
        boolean isUrlValid = urlValidator.isValid(url);
        if (!isUrlValid) {
            throw new InvalidUrlException();
        }
        if (urlCache.contains(url) || websiteRepository.existsByUrl(url)) {
            throw new WebsiteAlreadyExistsException();
        }
        downloadService.downloadWebsite(url);
    }

    /**
     * This method retrieves all downloaded websites.
     * @return List of all stored {@link Website} instances
     */
    public List<Website> getAllWebsites() {
        return websiteRepository.findAll();
    }

    /**
     * This method retrieves html of a downloaded {@link Website}.
     * @param id - id of {@link Website}
     * @return website's html code
     * @throws WebsiteNotFoundException when {@link Website} with given id is not stored
     */
    public String getWebsite(long id) throws WebsiteNotFoundException {
        return websiteRepository.findById(id)
                .map(Website::getHtml)
                .orElseThrow(WebsiteNotFoundException::new);
    }

    /**
     * This method deletes stored {@link Website}.
     * @param id - id of {@link Website} to be deleted
     * @throws WebsiteNotFoundException when {@link Website} with given id is not stored
     */
    public void deleteWebsite(long id) throws WebsiteNotFoundException {
        if (!websiteRepository.existsById(id)) {
            throw new WebsiteNotFoundException();
        }
        websiteRepository.deleteById(id);
    }
}
