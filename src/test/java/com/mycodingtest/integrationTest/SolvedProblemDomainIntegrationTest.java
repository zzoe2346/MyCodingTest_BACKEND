package com.mycodingtest.integrationTest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
@Tag("integration")
@Sql(scripts = {"/schema.sql", "/data.sql"})
class SolvedProblemDomainIntegrationTest {

    private RequestSpecification spec;

    @Value("${testJWT}")
    private String testJWT;

    @LocalServerPort
    private int port;

    private String SESSION_COOKIE;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        SESSION_COOKIE = String.format("mctApiAccessToken=%s; Path=/; Expires=Sun, 18 May 2025 07:22:03 GMT", testJWT);
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
    @DisplayName("푼 문제 목록을 페이징하여 조회한다")
    void getSolvedProblemWithReviewPage() {
        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("solved-problem/get-list",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
                                parameterWithName("size").description("페이지 크기").optional(),
                                parameterWithName("sort").description("정렬 기준 (예: id,desc)").optional()
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].solvedProblemId").description("푼 문제 ID"),
                                fieldWithPath("content[].problemNumber").description("백준 문제 번호"),
                                fieldWithPath("content[].problemTitle").description("문제 제목"),
                                fieldWithPath("content[].recentSubmitAt").description("최근 제출 시간").optional(),
                                fieldWithPath("content[].recentResultText").description("최근 제출 결과 텍스트"),
                                fieldWithPath("content[].isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("content[].reviewId").description("리뷰 ID"),
                                fieldWithPath("content[].difficultyLevel").description("체감 난이도").optional(),
                                fieldWithPath("content[].importanceLevel").description("재복습 필요도").optional(),
                                fieldWithPath("content[].isReviewed").description("리뷰 완료 여부"),
                                fieldWithPath("content[].reviewedAt").description("리뷰 일시"),
                                fieldWithPath("pageable").description("페이징 정보")
//                                fieldWithPath("pageable.pageNumber").description("현재 페이지 번호"),
//                                fieldWithPath("pageable.pageSize").description("페이지 크기"),
//                                fieldWithPath("pageable.sort").description("정렬 정보"),
//                                fieldWithPath("pageable.sort.empty").description("정렬 정보 존재 여부"),
//                                fieldWithPath("pageable.sort.sorted").description("정렬 여부"),
//                                fieldWithPath("pageable.sort.unsorted").description("정렬되지 않음 여부"),
//                                fieldWithPath("pageable.offset").description("오프셋"),
//                                fieldWithPath("pageable.paged").description("페이징 사용 여부"),
//                                fieldWithPath("pageable.unpaged").description("페이징 미사용 여부"),
//                                fieldWithPath("last").description("마지막 페이지 여부"),
//                                fieldWithPath("totalElements").description("전체 요소 수"),
//                                fieldWithPath("totalPages").description("전체 페이지 수"),
//                                fieldWithPath("first").description("첫 페이지 여부"),
//                                fieldWithPath("size").description("페이지 크기"),
//                                fieldWithPath("number").description("현재 페이지 번호"),
//                                fieldWithPath("sort").description("정렬 정보"),
//                                fieldWithPath("sort.empty").description("정렬 정보 존재 여부"),
//                                fieldWithPath("sort.sorted").description("정렬 여부"),
//                                fieldWithPath("sort.unsorted").description("정렬되지 않음 여부"),
//                                fieldWithPath("numberOfElements").description("현재 페이지 요소 수"),
//                                fieldWithPath("empty").description("결과 비어있음 여부")
                        )
                ))
                .when()
                .get("/api/solved-problems")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    @DisplayName("즐겨찾기 상태를 변경한다")
    void changeFavorite() {
        Long solvedProblemId = 1L;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .filter(document("solved-problem/change-favorite",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("solvedProblemId").description("해결한 문제 ID")
                        )
                ))
                .when()
                .patch("/api/solved-problems/{solvedProblemId}/favorite", solvedProblemId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("리뷰 상태에 따라 푼 문제를 조회한다")
    void getSolvedProblemByReviewStatus() {
        boolean isReviewed = true;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("solved-problem/get-by-review-status",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("isReviewed").description("리뷰 완료 여부 (true/false)")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
                                parameterWithName("size").description("페이지 크기").optional(),
                                parameterWithName("sort").description("정렬 기준").optional()
                        )
                ))
                .when()
                .get("/api/solved-problems/review/{isReviewed}", isReviewed)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    @DisplayName("즐겨찾기한 문제를 조회한다")
    void getFavoriteSolvedProblem() {
        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("solved-problem/get-favorites",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
                                parameterWithName("size").description("페이지 크기").optional(),
                                parameterWithName("sort").description("정렬 기준").optional()
                        )
                ))
                .when()
                .get("/api/solved-problems/favorites")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    @DisplayName("특정 태그를 가진 문제 목록을 조회한다")
    void getTaggedSolvedProblem() {
        Long tagId = 1L;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("solved-problem/get-by-tag",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("tagId").description("태그 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
                                parameterWithName("size").description("페이지 크기").optional(),
                                parameterWithName("sort").description("정렬 기준").optional()
                        )
                ))
                .when()
                .get("/api/solved-problems/tags/{tagId}", tagId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    @DisplayName("사용자의 태그 목록을 조회한다")
    void getMyTagList() {
        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("solved-problem/get-my-tags",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        responseFields(
                                fieldWithPath("tagIds").description("태그 ID")
                        )
                ))
                .when()
                .get("/api/solved-problems/tags")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("푼 문제를 삭제한다")
    void deleteSolvedProblem() {
        Long solvedProblemId = 1L;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .filter(document("solved-problem/delete",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("solvedProblemId").description("해결한 문제 ID")
                        )
                ))
                .when()
                .delete("/api/solved-problems/{solvedProblemId}", solvedProblemId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
