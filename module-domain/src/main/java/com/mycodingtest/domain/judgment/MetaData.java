package com.mycodingtest.domain.judgment;

/**
 * <h3>채점 메타데이터 인터페이스 (MetaData)</h3>
 * <p>
 * 각 플랫폼별 채점 상세 정보를 담기 위한 마커 인터페이스입니다.
 * 플랫폼마다 메타데이터 구조가 다르기 때문에 다형성을 활용하여 확장 가능하게 설계되었습니다.
 * </p>
 * <p>
 * <b>구현체 예시:</b>
 * <ul>
 * <li>{@link BojMetaData} - 백준 플랫폼 전용 메타데이터</li>
 * </ul>
 * </p>
 *
 * @see BojMetaData
 * @see Judgment#getMetaData()
 */
public interface MetaData {
}