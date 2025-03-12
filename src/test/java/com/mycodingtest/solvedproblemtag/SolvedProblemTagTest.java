package com.mycodingtest.solvedproblemtag;

import com.mycodingtest.solvedproblem.SolvedProblem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@Tag("unit")
class SolvedProblemTagTest {

    @Test
    @DisplayName("SolvedProblemTagId 객체의 equals와 hashCode 메서드 테스트")
    void solvedProblemTagIdEqualsAndHashCode() {
        // given
        SolvedProblemTag.SolvedProblemTagId id1 = new SolvedProblemTag.SolvedProblemTagId(1L, 5);
        SolvedProblemTag.SolvedProblemTagId id2 = new SolvedProblemTag.SolvedProblemTagId(1L, 5); // 동일한 값
        SolvedProblemTag.SolvedProblemTagId id3 = new SolvedProblemTag.SolvedProblemTagId(1L, 7); // 다른 태그
        SolvedProblemTag.SolvedProblemTagId id4 = new SolvedProblemTag.SolvedProblemTagId(2L, 5); // 다른 문제

        // then
        // 동일한 값을 가진 객체는 동등해야 함
        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());

        // 다른 태그 ID를 가진 객체는 동등하지 않아야 함
        assertThat(id1).isNotEqualTo(id3);
        assertThat(id1.hashCode()).isNotEqualTo(id3.hashCode());

        // 다른 SolvedProblem ID를 가진 객체는 동등하지 않아야 함
        assertThat(id1).isNotEqualTo(id4);
        assertThat(id1.hashCode()).isNotEqualTo(id4.hashCode());

        // null이나 다른 타입의 객체와 비교
        assertThat(id1).isNotEqualTo(null);
        assertThat(id1).isNotEqualTo("다른 타입의 객체");
    }

    @Test
    @DisplayName("기본 생성자로 SolvedProblemTag 객체 생성")
    void defaultConstructor() {
        // when
        SolvedProblem solvedProblem = mock(SolvedProblem.class);
        SolvedProblemTag tag = new SolvedProblemTag(solvedProblem, 1);

        // then
        assertThat(tag.getTagId()).isEqualTo(1);
    }

    @Test
    @DisplayName("solvedProblemTag는 no args 생성자로 객체가 생성되야한다.")
    void noArgConstructorSolvedProblemTag() {
        // given
        SolvedProblemTag solvedProblemTag = new SolvedProblemTag();
        // when then
        assertThat(solvedProblemTag).isNotNull();
    }

    @Test
    @DisplayName("solvedProblemTagId 또한 no args 생성자로 객체가 생성되야한다.")
    void noArgConstructorSolvedProblemTagId() {
        // given
        SolvedProblemTag.SolvedProblemTagId id = new SolvedProblemTag.SolvedProblemTagId();
        // when then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("solvedProblemTagId 비교시 같은 레퍼런스끼리 비교는 항상 true")
    void solvedProblemTagId_When_Same() {
        // given
        SolvedProblemTag.SolvedProblemTagId id = new SolvedProblemTag.SolvedProblemTagId();
        // when then
        assertThat(id.equals(id)).isTrue();
    }
}
