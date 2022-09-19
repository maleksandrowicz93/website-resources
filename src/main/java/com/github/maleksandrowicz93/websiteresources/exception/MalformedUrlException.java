package com.github.maleksandrowicz93.websiteresources.exception;

import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;

public class MalformedUrlException extends WebsiteResourcesException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MALFORMED_URL;

    public MalformedUrlException() {
        super(ERROR_CODE);
    }
}
