package com.github.maleksandrowicz93.websiteresources.exception;

import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;

public class WebsiteAlreadyExistsException extends WebsiteResourcesException {

    private static final ErrorCode ERROR_CODE = ErrorCode.WEBSITE_ALREADY_EXISTS;

    public WebsiteAlreadyExistsException() {
        super(ERROR_CODE);
    }
}
