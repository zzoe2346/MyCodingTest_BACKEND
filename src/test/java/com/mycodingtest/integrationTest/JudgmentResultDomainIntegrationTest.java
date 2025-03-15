package com.mycodingtest.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycodingtest.judgmentresult.dto.JudgmentResultSaveRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
@Tag("integration")
@Sql(scripts = {"/schema.sql", "/data.sql"})
class JudgmentResultDomainIntegrationTest {
    private RequestSpecification spec;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private final String SESSION_COOKIE = "mctApiAccessToken=eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhcGkiLCJ1c2VySWQiOjEsIm5hbWUiOiJTZW9uZ2h1biBKZW9uZyIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NKLVpBQlRjVV9ORS1lTUIyUm5WNkgtRHlZWHJ0YVEySXhrM3pZTVdjVzM3RFZfU0pSdD1zOTYtYyIsImlhdCI6MTczOTc3NjkyMywiZXhwIjo5OTk5OTk5OTk5fQ.wsn0nX48YX4OSsdJrIOkiOls-Tyty4TVKAZtLU-JdwQQ0s7ZTet6NmF7FwCN4jxl; Path=/; Expires=Sun, 18 May 2025 07:22:03 GMT";

    private JudgmentResultSaveRequest createJudgmentResultSaveRequest() {
        return new JudgmentResultSaveRequest(
                1L,
                "zzoe2346",
                1023,
                "1024",
                "맞았습니다!!",
                100,
                10,
                "java",
                100,
                LocalDateTime.now()
        );
    }

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
    @DisplayName("채점 결과를 저장하면 201 상태코드를 반환한다")
    void saveJudgmentResult() throws Exception {
        JudgmentResultSaveRequest request = createJudgmentResultSaveRequest();

        RestAssured.given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
                .filter(RestAssuredRestDocumentation.document("judgment-result/save",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("baekjoonId").description("백준 아이디"),
                                fieldWithPath("problemNumber").description("백준 문제 번호"),
                                fieldWithPath("problemTitle").description("백준 문제 제목"),
                                fieldWithPath("submissionId").description("제출 ID"),
                                fieldWithPath("resultText").description("채점 결과 텍스트"),
                                fieldWithPath("memory").description("사용한 메모리(KB)"),
                                fieldWithPath("time").description("실행 시간(ms)"),
                                fieldWithPath("language").description("사용 언어"),
                                fieldWithPath("codeLength").description("코드 길이(Byte)"),
                                fieldWithPath("submittedAt").description("제출 시간")
                        )
                ))
                .when()
                .post("/api/solved-problems/judgment-results")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("채점 결과 목록을 조회하면 200 상태코드와 결과 목록을 반환한다")
    void getJudgmentResultList() {
        Long solvedProblemId = 1L;

        RestAssured.given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(RestAssuredRestDocumentation.document("judgment-result/get-list",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("solvedProblemId").description("해결한 문제 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].submissionId").description("백준의 제출 ID"),
                                fieldWithPath("[].baekjoonId").description("백준 아이디"),
                                fieldWithPath("[].problemId").description("백준 문제 번호"),
                                fieldWithPath("[].resultText").description("백준 채점 결과 텍스트"),
                                fieldWithPath("[].memory").description("사용한 메모리(KB)"),
                                fieldWithPath("[].time").description("실행 시간(ms)"),
                                fieldWithPath("[].language").description("사용 언어"),
                                fieldWithPath("[].codeLength").description("코드 길이(Byte)"),
                                fieldWithPath("[].submittedAt").description("제출 시간"),
                                fieldWithPath("[].judgmentResultId").description("이 문제에 관한 우리 서버에 저장된 채점 결과 ID")
                        )
                ))
                .when()
                .get("/api/solved-problems/{solvedProblemId}/judgment-results", solvedProblemId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    @DisplayName("제출 ID 중복 체크시 중복되지 않으면 200 상태코드를 반환한다")
    void checkDuplicateSubmissionId() {
        String notDuplicatedSubmissionId = "1";

        RestAssured.given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .filter(RestAssuredRestDocumentation.document("judgment-result/check-duplicate-submission-id",
                        pathParameters(
                                parameterWithName("submissionId").description("제출 ID")
                        )
                ))
                .when()
                .get("/api/solved-problems/judgment-results/submission-id-check/{submissionId}", notDuplicatedSubmissionId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("코드 읽기 URL을 조회하면 200 상태코드와 URL을 반환한다")
    void getCodeReadUrl() {
        String submissionId = "12345";

        RestAssured.given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .filter(RestAssuredRestDocumentation.document("judgment-result/get-code-read-url",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("submissionId").description("제출 ID")
                        ),
                        responseFields(
                                fieldWithPath("url").description("코드 읽기 URL")
                        )
                ))
                .when()
                .get("/api/solved-problems/judgment-results/submission-code/read/{submissionId}", submissionId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("url", notNullValue());
    }

    @Test
    @DisplayName("코드 수정 URL을 조회하면 200 상태코드와 URL을 반환한다")
    void getCodeUpdateUrl() {
        String submissionId = "12345";

        RestAssured.given(this.spec)
                .header("Cookie", SESSION_COOKIE)
                .filter(RestAssuredRestDocumentation.document("judgment-result/get-code-update-url",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("submissionId").description("제출 ID")
                        ),
                        responseFields(
                                fieldWithPath("url").description("코드 수정 URL")
                        )
                ))
                .when()
                .get("/api/solved-problems/judgment-results/submission-code/update/{submissionId}", submissionId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("url", notNullValue());
    }
}
