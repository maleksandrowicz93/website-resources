package com.github.maleksandrowicz93.websiteresources.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * This class represents entity of stored website.
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String url;
    private String html;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Website website)) return false;
        return id != null
                && Objects.equals(id, website.id)
                && Objects.equals(url, website.url)
                && Objects.equals(html, website.html);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, html);
    }
}
