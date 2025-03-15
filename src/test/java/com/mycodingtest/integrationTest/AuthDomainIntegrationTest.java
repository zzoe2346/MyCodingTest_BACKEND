package com.mycodingtest.integrationTest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${testJWT}")
    private String testJWT;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
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
    public void testSignOut() {
        String sessionCookie = String.format("mctApiAccessToken=%s; Path=/; Expires=Sun, 18 May 2025 07:22:03 GMT", testJWT);

        RestAssured.given(this.spec)
                .filter(document("auth/sign-out"))
                .header("Cookie", sessionCookie)
                .when()
                .get("/api/sign-out")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .cookie("mctApiAccessToken", equalTo(""));
    }

    @Test
    public void testCheckSignIn_Success() {
        String sessionCookie = String.format("mctApiAccessToken=%s; Path=/; Expires=Sun, 18 May 2025 07:22:03 GMT", testJWT);

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
