package com.github.maleksandrowicz93.websiteresources.exception;

import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;

/**
 * This class represents exception thrown when not existing {@link com.github.maleksandrowicz93.websiteresources.model.Website}
 * is looked for.
 */
public class WebsiteNotFoundException extends WebsiteResourcesException {

    private static final ErrorCode ERROR_CODE = ErrorCode.WEBSITE_NOT_FOUND;

    public WebsiteNotFoundException() {
        super(ERROR_CODE);
    }
}
