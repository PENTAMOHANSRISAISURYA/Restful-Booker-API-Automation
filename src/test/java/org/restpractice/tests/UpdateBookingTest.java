package org.restpractice.tests;

import org.restpractice.base.BaseTest;
import org.restpractice.payloads.BookingPayload;
import org.restpractice.utils.PayloadFactory;
import org.restpractice.utils.TestContext;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UpdateBookingTest extends BaseTest {

    @BeforeClass
    public void setup() {
        ensureAuthSpec();
        ensureActiveBooking();
    }

    @Test(priority = 10, description = "Full update (PUT) with valid payload and token")
    public void testUpdateBooking_PUT_ValidPayload() {

        int bookingId = ensureActiveBooking();
        BookingPayload updatedPayload = PayloadFactory.updatedBooking();

        Response response = given()
                .spec(authRequestSpec)
                .pathParam("id", bookingId)
                .body(updatedPayload)
                .when()
                .put("/booking/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
                        "schemas/get_booking_schema.json"))
                .extract().response();

        Assert.assertEquals(response.jsonPath().getString("firstname"),
                updatedPayload.getFirstname());
        Assert.assertEquals(response.jsonPath().getString("lastname"),
                updatedPayload.getLastname());
        Assert.assertEquals(response.jsonPath().getInt("totalprice"),
                updatedPayload.getTotalprice());
        Assert.assertEquals(response.jsonPath().getBoolean("depositpaid"),
                updatedPayload.isDepositpaid());

        System.out.println("✅ Booking fully updated (PUT) for ID: " + bookingId);
    }

    @Test(priority = 11, description = "PUT without auth token returns 403")
    public void testUpdateBooking_PUT_NoAuth() {

        given()
                .spec(requestSpec)
                .pathParam("id", TestContext.getBookingId())
                .body(PayloadFactory.updatedBooking())
                .when()
                .put("/booking/{id}")
                .then()
                .log().all()
                .statusCode(403);
    }

    @Test(priority = 12, description = "Partial update (PATCH) — update firstname and lastname only")
    public void testUpdateBooking_PATCH_ValidPayload() {

        int bookingId = ensureActiveBooking();
        String partialBody = "{ \"firstname\": \"PatchedName\", \"lastname\": \"PatchedLastName\" }";

        Response response = given()
                .spec(authRequestSpec)
                .pathParam("id", bookingId)
                .body(partialBody)
                .when()
                .patch("/booking/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        Assert.assertEquals(response.jsonPath().getString("firstname"), "PatchedName");
        Assert.assertEquals(response.jsonPath().getString("lastname"), "PatchedLastName");
        Assert.assertEquals(response.jsonPath().getInt("totalprice"), 400,
                "Totalprice should remain from previous PUT");

        System.out.println("✅ Booking partially updated (PATCH) for ID: " + bookingId);
    }

    @Test(priority = 13, description = "PATCH without auth token returns 403")
    public void testUpdateBooking_PATCH_NoAuth() {

        given()
                .spec(requestSpec)
                .pathParam("id", TestContext.getBookingId())
                .body("{ \"firstname\": \"Unauthorized\" }")
                .when()
                .patch("/booking/{id}")
                .then()
                .log().all()
                .statusCode(403);
    }
}
