package com.github.maleksandrowicz93.websiteresources.repository;

import com.github.maleksandrowicz93.websiteresources.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This class represents repository storing {@link Website}.
 */
@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long> {

    boolean existsByUrl(String url);
    Optional<Website> findByUrl(String url);
}
