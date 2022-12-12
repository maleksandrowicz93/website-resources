package com.github.maleksandrowicz93.websiteresources.repository.jpa;

import com.github.maleksandrowicz93.websiteresources.model.Website;
import com.github.maleksandrowicz93.websiteresources.repository.generic.WebsiteRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This class represents repository storing {@link Website}.
 */
@Profile({"dev", "prod"})
@Repository
public interface JpaWebsiteRepository extends JpaRepository<Website, String>, WebsiteRepository {
}
