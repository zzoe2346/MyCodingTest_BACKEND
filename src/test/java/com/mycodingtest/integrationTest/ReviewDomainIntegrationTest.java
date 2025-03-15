package com.mycodingtest.integrationTest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
@Tag("integration")
@Sql(scripts = {"/schema.sql", "/data.sql"})
class ReviewDomainIntegrationTest {

    private RequestSpecification spec;
    @LocalServerPort
    private int port;

    private final String SESSION_COOKIE = "mctApiAccessToken=eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhcGkiLCJ1c2VySWQiOjEsIm5hbWUiOiJTZW9uZ2h1biBKZW9uZyIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NKLVpBQlRjVV9ORS1lTUIyUm5WNkgtRHlZWHJ0YVEySXhrM3pZTVdjVzM3RFZfU0pSdD1zOTYtYyIsImlhdCI6MTczOTc3NjkyMywiZXhwIjo5OTk5OTk5OTk5fQ.wsn0nX48YX4OSsdJrIOkiOls-Tyty4TVKAZtLU-JdwQQ0s7ZTet6NmF7FwCN4jxl; Path=/; Expires=Sun, 18 May 2025 07:22:03 GMT";


    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation)
                        .snippets()
                        .withDefaults(httpRequest(),httpResponse(),requestBody(),responseBody())
                        .and()
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @DisplayName("리뷰 정보를 조회한다")
    void getReview() {
        Long reviewId = 1L;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("review/get",
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        responseFields(
                                fieldWithPath("difficultyLevel").description("체감 난이도"),
                                fieldWithPath("importanceLevel").description("재복습 필요도"),
                                fieldWithPath("reviewed").description("리뷰 완료 여부"),
                                fieldWithPath("reviewedAt").description("리뷰 완료 시간")
                        )
                ))
                .when()
                .get("/api/solved-problems/reviews/{reviewId}", reviewId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("체감 난이도와 재복습 필요도를 수정한다")
    void updateReviewRatingLevels() {
        Long reviewId = 1L;
        String requestBody = """
                {
                    "difficultyLevel": 4,
                    "importanceLevel": 5
                }
                """;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .filter(document("review/update-levels",
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        requestFields(
                                fieldWithPath("difficultyLevel").description("체감 난이도 (1-5)"),
                                fieldWithPath("importanceLevel").description("재복습 필요도 (1-5)")
                        )
                ))
                .when()
                .put("/api/solved-problems/reviews/{reviewId}/levels", reviewId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("메모 수정/저장 URL을 획득한다")
    void getMemoUpdateUrl() {
        Long reviewId = 1L;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .filter(document("review/memo-update-url",
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        responseFields(
                                fieldWithPath("url").description("메모 수정/저장 URL")
                        )
                ))
                .when()
                .get("/api/solved-problems/reviews/{reviewId}/memo/update", reviewId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("url", notNullValue());
    }

    @Test
    @DisplayName("메모 읽기 URL을 획득한다")
    void getMemoReadUrl() {
        Long reviewId = 1L;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .filter(document("review/memo-read-url",
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        responseFields(
                                fieldWithPath("url").description("메모 읽기 URL")
                        )
                ))
                .when()
                .get("/api/solved-problems/reviews/{reviewId}/memo/read", reviewId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("url", notNullValue());
    }

    @Test
    @DisplayName("리뷰 상태를 완료로 전환한다")
    void updateReviewStatus() {
        Long reviewId = 1L;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .filter(document("review/update-status",
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        responseFields(
                                fieldWithPath("reviewed").description("리뷰 완료 여부"),
                                fieldWithPath("reviewedAt").description("리뷰 완료 시간")
                        )
                ))
                .when()
                .put("/api/solved-problems/reviews/{reviewId}/status", reviewId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("reviewed", equalTo(true))
                .body("reviewedAt", notNullValue());
    }

    @Test
    @DisplayName("리뷰 대기 중인 문제 개수를 조회한다")
    void getWaitReviewCount() {
        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .filter(document("review/unreviewed-count",
                        responseFields(
                                fieldWithPath("count").description("리뷰 대기 중인 문제 개수")
                        )
                ))
                .when()
                .get("/api/solved-problems/unreviewed-count")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("count", greaterThanOrEqualTo(0));
    }
}
