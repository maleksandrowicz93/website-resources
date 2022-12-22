package com.github.maleksandrowicz93.websiteresources.repository.generic;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID> {

    List<T> findAll();
    <S extends T> S save(S entity);
    boolean existsById(ID id);
    Optional<T> findById(ID id);
    void deleteById(ID id);
    void deleteAll();
}
