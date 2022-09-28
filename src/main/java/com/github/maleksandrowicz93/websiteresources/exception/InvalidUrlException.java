package com.github.maleksandrowicz93.websiteresources.exception;

import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;

/**
 * This class represents exception thrown when there is tried to process malformed URL.
 */
public class InvalidUrlException  extends WebsiteResourcesException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_URL;

    public InvalidUrlException() {
        super(ERROR_CODE);
    }
}
