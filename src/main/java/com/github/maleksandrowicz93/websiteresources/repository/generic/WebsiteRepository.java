package com.github.maleksandrowicz93.websiteresources.repository.generic;

import com.github.maleksandrowicz93.websiteresources.model.Website;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface WebsiteRepository extends BaseRepository<Website, String> {

    boolean existsByUrl(String url);
    Optional<Website> findByUrl(String url);
}
