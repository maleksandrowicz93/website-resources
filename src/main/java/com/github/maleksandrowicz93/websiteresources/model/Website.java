package com.github.maleksandrowicz93.websiteresources.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Objects;

/**
 * This class represents entity of stored website.
 */
@Entity
@Document
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Website {

    @org.springframework.data.annotation.Id
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(unique = true, nullable = false)
    @Indexed(unique = true)
    private String url;
    private String html;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Website)) return false;
        Website website = (Website) o;
        return id != null
                && Objects.equals(id, website.id)
                && Objects.equals(url, website.url)
                && Objects.equals(html, website.html);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, html);
    }
}
