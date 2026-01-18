package com.mycodingtest.infra.judgment;

import com.mycodingtest.domain.judgment.BojMetaData;
import com.mycodingtest.domain.judgment.MetaData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MetaDataConverterTest {

    private final MetaDataConverter converter = new MetaDataConverter();

    @Nested
    class DB_컬럼_변환 {

        @Test
        void MetaData를_JSON_문자열로_변환한다() {
            // given
            BojMetaData metaData = BojMetaData.builder()
                    .submissionId(12345L)
                    .baekjoonId("testUser")
                    .resultText("맞았습니다!!")
                    .memory(1024)
                    .time(100)
                    .language("Java 11")
                    .codeLength(500)
                    .build();

            // when
            String json = converter.convertToDatabaseColumn(metaData);

            // then
            assertThat(json).isNotNull();
            assertThat(json).contains("testUser");
            assertThat(json).contains("12345");
        }

        @Test
        void null_MetaData는_null을_반환한다() {
            // given
            MetaData metaData = null;

            // when
            String json = converter.convertToDatabaseColumn(metaData);

            // then
            assertThat(json).contains("null");
        }
    }

    @Nested
    class Entity_속성_변환 {

        @Test
        void BOJ_타입의_JSON을_BojMetaData로_변환한다() {
            // given
            BojMetaData dbMetaData = BojMetaData.builder()
                    .submissionId(12345L)
                    .baekjoonId("testUser")
                    .resultText("맞았습니다!!")
                    .memory(1024)
                    .time(100)
                    .language("Java 11")
                    .codeLength(500)
                    .build();

            // when
            String json = converter.convertToDatabaseColumn(dbMetaData);

            // when
            MetaData metaData = converter.convertToEntityAttribute(json);

            // then
            assertThat(metaData).isInstanceOf(BojMetaData.class);
            BojMetaData bojMetaData = (BojMetaData) metaData;
            assertThat(bojMetaData.getSubmissionId()).isEqualTo(12345L);
            assertThat(bojMetaData.getBaekjoonId()).isEqualTo("testUser");
            assertThat(bojMetaData.getResultText()).isEqualTo("맞았습니다!!");
            assertThat(bojMetaData.getMemory()).isEqualTo(1024);
        }

        @Test
        void 알_수_없는_타입의_JSON은_null을_반환한다() {
            // given
            String json = """
                    {"type":"UNKNOWN","someField":"value"}
                    """;

            // when
            MetaData metaData = converter.convertToEntityAttribute(json);

            // then
            assertThat(metaData).isNull();
        }
    }

    @Nested
    class 왕복_변환 {

        @Test
        void MetaData를_JSON으로_변환_후_다시_MetaData로_변환하면_동일하다() {
            // given
            BojMetaData originalMetaData = BojMetaData.builder()
                    .submissionId(99999L)
                    .baekjoonId("roundTrip")
                    .resultText("틀렸습니다")
                    .memory(2048)
                    .time(500)
                    .language("Python 3")
                    .codeLength(100)
                    .build();

            // when
            String json = converter.convertToDatabaseColumn(originalMetaData);
            MetaData restoredMetaData = converter.convertToEntityAttribute(json);

            // then
            assertThat(restoredMetaData).isInstanceOf(BojMetaData.class);
            BojMetaData restored = (BojMetaData) restoredMetaData;
            assertThat(restored.getSubmissionId()).isEqualTo(originalMetaData.getSubmissionId());
            assertThat(restored.getBaekjoonId()).isEqualTo(originalMetaData.getBaekjoonId());
            assertThat(restored.getResultText()).isEqualTo(originalMetaData.getResultText());
            assertThat(restored.getMemory()).isEqualTo(originalMetaData.getMemory());
            assertThat(restored.getTime()).isEqualTo(originalMetaData.getTime());
            assertThat(restored.getLanguage()).isEqualTo(originalMetaData.getLanguage());
            assertThat(restored.getCodeLength()).isEqualTo(originalMetaData.getCodeLength());
        }
    }
}
