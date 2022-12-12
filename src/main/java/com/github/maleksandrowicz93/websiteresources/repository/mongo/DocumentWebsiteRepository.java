package com.github.maleksandrowicz93.websiteresources.repository.mongo;

import com.github.maleksandrowicz93.websiteresources.model.Website;
import com.github.maleksandrowicz93.websiteresources.repository.generic.WebsiteRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Profile("mongo")
@Repository
public interface DocumentWebsiteRepository extends MongoRepository<Website, String>, WebsiteRepository {
}
