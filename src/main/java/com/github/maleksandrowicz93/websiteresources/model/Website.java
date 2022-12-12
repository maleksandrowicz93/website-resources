package com.github.maleksandrowicz93.websiteresources.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

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
}
