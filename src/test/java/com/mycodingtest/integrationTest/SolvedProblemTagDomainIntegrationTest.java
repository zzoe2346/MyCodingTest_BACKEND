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
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
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
class SolvedProblemTagDomainIntegrationTest {

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
                        .withDefaults(httpRequest(), httpResponse(), requestBody(), responseBody())
                        .and()
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @DisplayName("문제에 태그를 재설정한다")
    void setTags() {
        Long solvedProblemId = 1L;
        String requestBody = """
                {
                    "tagIds": [1, 3, 5]
                }
                """;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .filter(document("solved-problem-tag/set-tags",
                        pathParameters(
                                parameterWithName("solvedProblemId").description("해결한 문제 ID")
                        ),
                        requestFields(
                                fieldWithPath("tagIds").description("설정할 태그 ID 배열 (최대 7개)")
                        )
                ))
                .when()
                .put("/api/solved-problems/{solvedProblemId}/tags", solvedProblemId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("태그 개수가 7개를 초과하면 Bad Request를 반환한다")
    void setTags_withTooManyTags() {
        Long solvedProblemId = 1L;
        String requestBody = """
                {
                    "tagIds": [1, 2, 3, 4, 5, 6, 7, 8]
                }
                """;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .put("/api/solved-problems/{solvedProblemId}/tags", solvedProblemId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("문제의 태그를 조회한다")
    void getTags() {
        Long solvedProblemId = 1L;

        given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("solved-problem-tag/get-tags",
                        pathParameters(
                                parameterWithName("solvedProblemId").description("해결한 문제 ID")
                        ),
                        responseFields(
                                fieldWithPath("tagIds").description("문제에 설정된 태그 ID 배열")
                        )
                ))
                .when()
                .get("/api/solved-problems/{solvedProblemId}/tags", solvedProblemId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("tagIds", notNullValue());
    }


}
