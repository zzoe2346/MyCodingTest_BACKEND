package com.mycodingtest.application.review.dto;

import com.mycodingtest.domain.common.DomainPage;

import java.util.List;

public record PagedResult<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize,
        boolean isLast
) {
    public static <T> PagedResult<T> from(
            List<T> content,
            DomainPage<?> domainPage
    ) {
        return new PagedResult<>(content, domainPage.totalElements(), domainPage.totalPages(), domainPage.pageNumber(), domainPage.pageSize(), domainPage.isLast());
    }
}
