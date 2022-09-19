package com.github.maleksandrowicz93.websiteresources.exception;

import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;
import lombok.Getter;

@Getter
public abstract class WebsiteResourcesException extends Exception {

    private final ErrorCode errorCode;

    public WebsiteResourcesException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
