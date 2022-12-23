package com.github.maleksandrowicz93.websiteresources.annotation;

import com.github.maleksandrowicz93.websiteresources.config.Profiles;
import com.github.maleksandrowicz93.websiteresources.config.TestConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EmbeddedKafkaTest
@Import(TestConfig.class)
@ActiveProfiles(profiles = Profiles.DEV)
@AutoConfigureMockMvc
public @interface IntegrationTest {
}
