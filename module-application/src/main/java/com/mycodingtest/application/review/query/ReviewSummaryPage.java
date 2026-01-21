package com.mycodingtest.application.review.query;

import com.mycodingtest.domain.common.DomainPage;

import java.util.List;

public record ReviewSummaryPage<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize,
        boolean isLast
) {
    public static <T> ReviewSummaryPage<T> from(
            List<T> content,
            DomainPage<?> domainPage
    ) {
        return new ReviewSummaryPage<>(content, domainPage.totalElements(), domainPage.totalPages(), domainPage.pageNumber(), domainPage.pageSize(), domainPage.isLast());
    }
}
