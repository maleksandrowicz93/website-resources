package com.github.maleksandrowicz93.websiteresources.handler;

import com.github.maleksandrowicz93.websiteresources.dto.ErrorResponseDto;
import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteResourcesException;
import com.github.maleksandrowicz93.websiteresources.utils.ResponseFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

/**
 * This class contains global exception handlers.
 */
@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method handles all thrown {@link WebsiteResourcesException} exceptions.
     * @param e - thrown exception
     * @return {@link ResponseEntity} instance
     */
    @ExceptionHandler(WebsiteResourcesException.class)
    public ResponseEntity<ErrorResponseDto> handleWebsiteResourcesException(WebsiteResourcesException e) {
        ErrorCode errorCode = e.getErrorCode();
        UUID uuid = UUID.randomUUID();
        log.error("Error with UUID {}: {}", uuid, e.getMessage());
        return ResponseFactory.response(errorCode, uuid);
    }

    /**
     * This method handles all thrown all unexpected {@link Throwable} exceptions.
     * @param throwable - thrown exception
     * @return {@link ResponseEntity} instance
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponseDto> handleThrowable(Throwable throwable) {
        UUID uuid = UUID.randomUUID();
        log.error("Error with UUID {}: {}", uuid, throwable.getMessage());
        return ResponseFactory.response(ErrorCode.UNKNOWN_ERROR, uuid);
    }
}
