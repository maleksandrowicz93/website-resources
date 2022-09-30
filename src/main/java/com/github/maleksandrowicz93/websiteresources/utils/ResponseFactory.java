package com.github.maleksandrowicz93.websiteresources.utils;

import com.github.maleksandrowicz93.websiteresources.dto.ErrorResponseDto;
import com.github.maleksandrowicz93.websiteresources.dto.ResponseDto;
import com.github.maleksandrowicz93.websiteresources.enums.ErrorCode;
import com.github.maleksandrowicz93.websiteresources.enums.ResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

/**
 * This class is factory for API responses.
 */
public class ResponseFactory {

    private ResponseFactory() {}

    /**
     * This method builds response with {@link ResponseDto} type body.
     * @param responseMessage - {@link ResponseMessage} constant
     * @return {@link ResponseEntity} with {@link ResponseDto} body
     */
    public static ResponseEntity<ResponseDto> response(ResponseMessage responseMessage) {
        ResponseDto responseDto = responseDto(responseMessage);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * This method builds {@link ResponseDto} instance.
     * @param responseMessage - {@link ResponseMessage} constant
     * @return {@link ResponseDto} instance
     */
    public static ResponseDto responseDto(ResponseMessage responseMessage) {
        return ResponseDto.builder()
                .code(responseMessage.getCode())
                .message(responseMessage.getMessage())
                .build();
    }

    /**
     * This method builds response with {@link ErrorResponseDto} type body.
     * @param errorCode - {@link ErrorCode} constant
     * @param uuid - {@link UUID} generated value
     * @return {@link ResponseEntity} with {@link ErrorResponseDto} body
     */
    public static ResponseEntity<ErrorResponseDto> response(ErrorCode errorCode, UUID uuid) {
        ErrorResponseDto errorResponse = errorResponseDto(errorCode, uuid);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    /**
     * This method builds {@link ErrorResponseDto} instance.
     * @param errorCode - {@link ErrorCode} constant
     * @param uuid - {@link UUID} generated value
     * @return {@link ErrorResponseDto} instance
     */
    public static ErrorResponseDto errorResponseDto(ErrorCode errorCode, UUID uuid) {
        return ErrorResponseDto.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .uuid(uuid.toString())
                .build();
    }
}
