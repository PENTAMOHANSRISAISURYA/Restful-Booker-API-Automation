package org.restpractice.tests;

import org.restpractice.base.BaseTest;
import org.restpractice.utils.PayloadFactory;
import org.restpractice.utils.TestContext;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AuthTest extends BaseTest {

    @Test(priority = 1, description = "Generate auth token with valid credentials")
    public void testGenerateToken_ValidCredentials() {

        Response response = given()
                .spec(requestSpec)
                .body(PayloadFactory.authCredentials())
                .when()
                .post("/auth")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String token = response.jsonPath().getString("token");

        Assert.assertNotNull(token, "Token should not be null");
        Assert.assertFalse(token.isEmpty(), "Token should not be empty");
        Assert.assertNotEquals(token, "Bad credentials", "Credentials should be valid");

        TestContext.setToken(token);
        buildAuthSpec(token);

        System.out.println("Auth Token Generated: " + token);
    }

    @Test(priority = 2, description = "Auth returns 'Bad credentials' for wrong password")
    public void testGenerateToken_InvalidCredentials() {

        String invalidCreds = "{ \"username\": \"admin\", \"password\": \"wrongpassword\" }";

        Response response = given()
                .spec(requestSpec)
                .body(invalidCreds)
                .when()
                .post("/auth")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String reason = response.jsonPath().getString("reason");
        Assert.assertEquals(reason, "Bad credentials");
    }
}