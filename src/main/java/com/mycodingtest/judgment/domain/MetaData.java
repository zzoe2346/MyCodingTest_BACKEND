package com.mycodingtest.judgment.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * <h3>메타데이터 인터페이스 (MetaData)</h3>
 * <p>
 * 플랫폼별로 상이한 채점 상세 정보를 담기 위한 마커 인터페이스입니다.
 * </p>
 * <p>
 * <b>다형성 적용:</b><br>
 * Jackson 라이브러리의 {@link JsonTypeInfo}를 사용하여, JSON 데이터 내의 "type" 필드 값에 따라
 * 구체적인 구현 클래스(예: {@link BojMetaData})로 자동 매핑됩니다.
 * 이를 통해 DB 스키마 변경 없이 새로운 플랫폼(프로그래머스, LeetCode 등)의 메타데이터를 확장할 수 있습니다.
 * </p>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // JSON에 "type": "BOJ" 같은 필드가 추가되어 구분자로 쓰입니다.
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BojMetaData.class, name = "BOJ")
        // 추후 프로그래머스 등이 추가되면 여기에 @JsonSubTypes.Type(value = PgsMetaData.class, name = "PROG") 추가하여 확장
})
public interface MetaData {
}