package com.wanaia.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    boolean success,
    T data,
    PageMeta meta,
    ErrorDetail error,
    Instant timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null, Instant.now());
    }

    public static <T> ApiResponse<T> success(T data, PageMeta meta) {
        return new ApiResponse<>(true, data, meta, null, Instant.now());
    }

    public static <T> ApiResponse<T> failure(ErrorDetail error) {
        return new ApiResponse<>(false, null, null, error, Instant.now());
    }

    public record PageMeta(
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean isLast
    ) {}

    public record ErrorDetail(
        String code,
        String message,
        int status,
        String path,
        java.util.List<ValidationErrorItem> validationErrors
    ) {}

    public record ValidationErrorItem(
        String field,
        Object rejectedValue,
        String message
    ) {}
}
