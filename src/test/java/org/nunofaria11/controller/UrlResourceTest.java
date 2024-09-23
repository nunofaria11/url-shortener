package org.nunofaria11.controller;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.mongodb.MongoTestResource;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(value = MongoTestResource.class, initArgs = {@ResourceArg(name = MongoTestResource.PORT, value = "27017")})
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class UrlResourceTest {

    @Test
    @Order(1)
    void testPost() {
        ValidatableResponse response = given()
                .when()
                .contentType("application/json")
                .body("""
                        {
                            "url": "https://www.google.com"
                        }
                        """)
                .post("/")
                .then()
                .statusCode(200)
                .body("shortUrl", Matchers.matchesPattern("https://url-shortener.nunofaria11.org/[a-z0-9]{6}"));

    }

    @org.junit.jupiter.api.Nested
    class ExistingShortenUrl {

        String hash;

        @BeforeEach
        void setup() {
            Response post = given()
                    .when()
                    .contentType("application/json")
                    .body("""
                            {
                                "url": "https://www.google.com"
                            }
                            """)
                    .post("/");
            String shortUrl = post.body().jsonPath().getString("shortUrl");
            hash = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);
        }

        @Test
        void testGet() {
            given()
                    .when()
                    .get("/info/" + hash)
                    .then()
                    .statusCode(200);
        }

        @Test
        void testGetRedirect() {
            given()
                    .when()
                    .redirects().follow(false)
                    .get("/" + hash)
                    .then()
                    .statusCode(301)
                    .header("Location", "https://www.google.com");
        }

    }

}
