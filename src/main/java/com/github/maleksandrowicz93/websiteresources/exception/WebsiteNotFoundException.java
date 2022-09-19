package com.github.maleksandrowicz93.websiteresources.exception;

import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;

public class WebsiteNotFoundException extends WebsiteResourcesException {

    private static final ErrorCode ERROR_CODE = ErrorCode.WEBSITE_NOT_FOUND;

    public WebsiteNotFoundException() {
        super(ERROR_CODE);
    }
}
