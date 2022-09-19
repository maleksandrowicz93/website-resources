package com.github.maleksandrowicz93.websiteresources.exception;

import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;

/**
 * This class represents exception thrown when it is tried to add existing
 * {@link com.github.maleksandrowicz93.websiteresources.entity.Website}.
 */
public class WebsiteAlreadyExistsException extends WebsiteResourcesException {

    private static final ErrorCode ERROR_CODE = ErrorCode.WEBSITE_ALREADY_EXISTS;

    public WebsiteAlreadyExistsException() {
        super(ERROR_CODE);
    }
}
