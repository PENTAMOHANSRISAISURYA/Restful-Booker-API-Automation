package org.restpractice.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.restpractice.payloads.BookingPayload;
import org.restpractice.utils.PayloadFactory;
import org.restpractice.utils.TestContext;

import static io.restassured.RestAssured.given;


public class BaseTest {

    protected static RequestSpecification requestSpec;
    protected static RequestSpecification authRequestSpec;
    protected static String authToken;

    static {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) RestAssured")
                .log(LogDetail.ALL)
                .build();
    }

    protected static void buildAuthSpec(String token) {
        authToken = token;
        authRequestSpec = new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .addCookie("token", token)
                .build();
    }

    protected static void ensureAuthSpec() {
        if (authRequestSpec != null && authToken != null && !authToken.isBlank()) {
            return;
        }

        String existingToken = TestContext.getToken();
        if (existingToken != null && !existingToken.isBlank()) {
            buildAuthSpec(existingToken);
            return;
        }

        Response response = given()
                .spec(requestSpec)
                .body(PayloadFactory.authCredentials())
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().response();

        String token = response.jsonPath().getString("token");
        TestContext.setToken(token);
        buildAuthSpec(token);
    }

    protected static int ensureActiveBooking() {
        int bookingId = TestContext.getBookingId();
        if (bookingId > 0) {
            int status = given()
                    .spec(requestSpec)
                    .pathParam("id", bookingId)
                    .when()
                    .get("/booking/{id}")
                    .then()
                    .extract().statusCode();

            if (status == 200) {
                return bookingId;
            }
        }

        BookingPayload payload = PayloadFactory.defaultBooking();
        Response response = given()
                .spec(requestSpec)
                .body(payload)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().response();

        int newBookingId = response.jsonPath().getInt("bookingid");
        TestContext.setBookingId(newBookingId);
        return newBookingId;
    }
}
