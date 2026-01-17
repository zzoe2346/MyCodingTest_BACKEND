package com.mycodingtest.domain.common;

import java.util.List;

/**
 * <h3>도메인 페이징 결과 (DomainPage)</h3>
 * <p>
 * 도메인 계층에서 사용하는 기술 독립적인 페이징 결과 객체입니다.
 * Spring Data의 {@code Page} 인터페이스에 의존하지 않고 순수 도메인 영역에서
 * 페이징된 데이터를 전달할 수 있도록 설계되었습니다.
 * </p>
 *
 * @param <T>           페이징되는 데이터의 타입
 * @param content       현재 페이지의 데이터 목록
 * @param totalElements 전체 데이터 개수
 * @param totalPages    전체 페이지 수
 * @param pageNumber    현재 페이지 번호 (0부터 시작)
 * @param pageSize      페이지당 데이터 개수
 * @param isLast        마지막 페이지 여부
 */
public record DomainPage<T>(List<T> content,
                            long totalElements,
                            int totalPages,
                            int pageNumber,
                            int pageSize,
                            boolean isLast) {
}
