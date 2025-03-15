package com.mycodingtest.integrationTest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
@Tag("integration")
@Sql(scripts = {"/schema.sql", "/data.sql"})
class AuthDomainIntegrationTest {

    private RequestSpecification spec;
    @LocalServerPort
    private int port;

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
    public void testSignOut() {
        String sessionCookie = "mctApiAccessToken=eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhcGkiLCJ1c2VySWQiOjEsIm5hbWUiOiJTZW9uZ2h1biBKZW9uZyIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NKLVpBQlRjVV9ORS1lTUIyUm5WNkgtRHlZWHJ0YVEySXhrM3pZTVdjVzM3RFZfU0pSdD1zOTYtYyIsImlhdCI6MTczOTc3NjkyMywiZXhwIjo5OTk5OTk5OTk5fQ.wsn0nX48YX4OSsdJrIOkiOls-Tyty4TVKAZtLU-JdwQQ0s7ZTet6NmF7FwCN4jxl; Path=/; Expires=Sun, 18 May 2025 07:22:03 GMT"; // 실제 로그인 후 쿠키로 대체

        RestAssured.given(this.spec)
                .filter(document("auth/sign-out"))
                .header("Cookie", sessionCookie)
                .when()
                .get("/api/sign-out")
                .then()
                .statusCode(HttpStatus.OK.value())
                .cookie("mctApiAccessToken", equalTo(""));
    }

    @Test
    public void testCheckSignIn_Success() {
        String sessionCookie = "mctApiAccessToken=eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhcGkiLCJ1c2VySWQiOjEsIm5hbWUiOiJTZW9uZ2h1biBKZW9uZyIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NKLVpBQlRjVV9ORS1lTUIyUm5WNkgtRHlZWHJ0YVEySXhrM3pZTVdjVzM3RFZfU0pSdD1zOTYtYyIsImlhdCI6MTczOTc3NjkyMywiZXhwIjo5OTk5OTk5OTk5fQ.wsn0nX48YX4OSsdJrIOkiOls-Tyty4TVKAZtLU-JdwQQ0s7ZTet6NmF7FwCN4jxl; Path=/; Expires=Sun, 18 May 2025 07:22:03 GMT"; // 실제 로그인 후 쿠키로 대체


        RestAssured.given(this.spec)
                .header("Cookie", sessionCookie)
                .filter(document("auth/check-sign-in",
                        responseFields(
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("picture").description("프로필 사진 URL")
                        )
                ))
                .when()
                .get("/api/me")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testCheckSignIn_Unauthorized() {
        RestAssured.given(this.spec)
                .when()
                .get("/api/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

    }
}
