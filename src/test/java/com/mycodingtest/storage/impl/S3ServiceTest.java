package com.mycodingtest.storage.impl;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@MockitoSettings
class S3ServiceTest {

    @Mock
    private S3Template s3Template;

    @InjectMocks
    private S3Service s3Service;

    private static final String BUCKET_NAME = "test-bucket";
    private static final int URL_EXPIRATION_SECONDS = 3600;
    private static final String MOCK_URL = "https://mock-url.com";

    @BeforeEach
    void setTestProperties() {
        s3Service = new S3Service(s3Template);
        ReflectionTestUtils.setField(s3Service, "bucketName", BUCKET_NAME);
        ReflectionTestUtils.setField(s3Service, "urlExpirationSeconds", URL_EXPIRATION_SECONDS);
    }


    @Test
    @DisplayName("코드 읽기 URL을 생성한다")
    void getCodeReadUrl() throws Exception {
        // given
        String submissionId = "submission1";
        Long userId = 1L;
        String expectedKey = "1/codes/submission1.txt";
        given(s3Template.createSignedGetURL(eq(BUCKET_NAME), eq(expectedKey), any(Duration.class)))
                .willReturn(URI.create(MOCK_URL).toURL());

        // when
        String url = s3Service.getCodeReadUrl(submissionId, userId);

        // then
        assertThat(url).isEqualTo(MOCK_URL);
    }

    @Test
    @DisplayName("코드 업데이트 URL을 생성한다")
    void getCodeUpdateUrl() throws Exception {
        // given
        String submissionId = "submission1";
        Long userId = 1L;
        String expectedKey = "1/codes/submission1.txt";

        given(s3Template.createSignedPutURL(eq(BUCKET_NAME), eq(expectedKey), any(Duration.class)))
                .willReturn(URI.create(MOCK_URL).toURL());

        // when
        String url = s3Service.getCodeUpdateUrl(submissionId, userId);

        // then
        assertThat(url).isEqualTo(MOCK_URL);
    }

    @Test
    @DisplayName("메모 업데이트 URL을 생성한다")
    void getMemoUpdateUrl() throws Exception {
        // given
        String reviewId = "review1";
        Long userId = 1L;
        String expectedKey = "1/memos/review1.txt";

        given(s3Template.createSignedPutURL(eq(BUCKET_NAME), eq(expectedKey), any(Duration.class)))
                .willReturn(URI.create(MOCK_URL).toURL());

        // when
        String url = s3Service.getMemoUpdateUrl(reviewId, userId);

        // then
        assertThat(url).isEqualTo(MOCK_URL);
    }

    @Test
    @DisplayName("메모를 삭제한다")
    void deleteMemo() {
        // given
        String reviewId = "review1";
        Long userId = 1L;
        String expectedKey = "1/memos/review1.txt";

        // when
        s3Service.deleteMemo(reviewId, userId);

        // then
        verify(s3Template).deleteObject(BUCKET_NAME, expectedKey);
    }

    @Nested
    @DisplayName("메모 읽기 기능 테스트")
    class getMemoReadUrl {
        @Test
        @DisplayName("메모가 존재할 때 메모 읽기 URL을 생성한다")
        void getMemoReadUrl_WhenMemoExists() throws Exception {
            // given
            String reviewId = "review1";
            Long userId = 1L;
            String expectedKey = "1/memos/review1.txt";

            given(s3Template.objectExists(BUCKET_NAME, expectedKey)).willReturn(true);
            given(s3Template.createSignedGetURL(eq(BUCKET_NAME), eq(expectedKey), any(Duration.class)))
                    .willReturn(URI.create(MOCK_URL).toURL());

            // when
            String url = s3Service.getMemoReadUrl(reviewId, userId);

            // then
            assertThat(url).isEqualTo(MOCK_URL);
        }

        @Test
        @DisplayName("메모가 존재하지 않을 때 noMemo를 반환한다")
        void getMemoReadUrl_WhenMemoNotExists() {
            // given
            String reviewId = "review1";
            Long userId = 1L;
            String expectedKey = "1/memos/review1.txt";

            given(s3Template.objectExists(BUCKET_NAME, expectedKey)).willReturn(false);

            // when
            String result = s3Service.getMemoReadUrl(reviewId, userId);

            // then
            assertThat(result).isEqualTo("noMemo");
        }
    }

    @Test
    @DisplayName("코드 삭제를 수행한다")
    void deleteCodes() {
        // given
        List<String> submissionIds = List.of("sub1", "sub2");
        Long userId = 1L;

        // when
        s3Service.deleteCodes(submissionIds, userId);

        // then
        verify(s3Template).deleteObject(BUCKET_NAME, "1/codes/sub1.txt");
        verify(s3Template).deleteObject(BUCKET_NAME, "1/codes/sub2.txt");
    }
}
