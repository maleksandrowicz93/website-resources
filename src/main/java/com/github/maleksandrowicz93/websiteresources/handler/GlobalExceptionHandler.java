package com.github.maleksandrowicz93.websiteresources.handler;

import com.github.maleksandrowicz93.websiteresources.dto.ErrorResponseDto;
import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;
import com.github.maleksandrowicz93.websiteresources.exception.WebsiteResourcesException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebsiteResourcesException.class)
    public ResponseEntity<ErrorResponseDto> handleWebsiteResourcesException(WebsiteResourcesException e) {
        ErrorCode errorCode = e.getErrorCode();
        UUID uuid = UUID.randomUUID();
        log.error("Error with UUID {}: {}", uuid, e.getMessage());
        return buildResponseEntity(errorCode, uuid);
    }

    private ResponseEntity<ErrorResponseDto> buildResponseEntity(ErrorCode errorCode, UUID uuid) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .uuid(uuid.toString())
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponseDto> handleThrowable(Throwable throwable) {
        UUID uuid = UUID.randomUUID();
        log.error("Error with UUID {}: {}", uuid, throwable.getMessage());
        return buildResponseEntity(ErrorCode.UNKNOWN_ERROR, uuid);
    }
}
