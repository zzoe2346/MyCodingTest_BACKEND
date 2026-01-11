package com.mycodingtest.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * <h3>공통 엔티티 기틀 (BaseEntity)</h3>
 * <p>
 * 모든 도메인 엔티티에서 공통적으로 사용하는 식별자(ID)와 생성/수정 시간을 관리합니다.
 * JPA Auditing을 활용하여 데이터의 생명주기를 자동으로 추적합니다.
 * </p>
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * 시스템에서 사용하는 고유 식별자 (Auto Increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 레코드 생성 일시
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 레코드 최근 수정 일시
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

}