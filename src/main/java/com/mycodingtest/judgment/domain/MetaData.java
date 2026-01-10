package com.mycodingtest.judgment.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // JSON에 "type": "BOJ" 같은 필드가 추가되어 구분자로 쓰입니다.
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BojMetaData.class, name = "BOJ")
        // 나중에 프로그래머스 등이 추가되면 여기에 @JsonSubTypes.Type(value = PgsMetaData.class, name = "PROG") 추가
})
public interface MetaData {
}
