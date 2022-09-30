package com.github.maleksandrowicz93.websiteresources.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * This enum represents context information for errors.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    WEBSITE_ALREADY_EXISTS("WEBSITE_ALREADY_EXISTS", "Website already exists", HttpStatus.BAD_REQUEST),
    WEBSITE_NOT_FOUND("WEBSITE_NOT_FOUND", "Website not found", HttpStatus.NOT_FOUND),
    WEBSITE_DOWNLOADING_FAILED("WEBSITE_DOWNLOADING_FAILED", "Website downloading failed", HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_ERROR("UNKNOWN_ERROR", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_URL("INVALID_URL", "Url is invalid", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
