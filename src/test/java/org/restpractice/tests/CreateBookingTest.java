package org.restpractice.tests;

import org.restpractice.base.BaseTest;
import org.restpractice.payloads.BookingPayload;
import org.restpractice.utils.PayloadFactory;
import org.restpractice.utils.TestContext;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CreateBookingTest extends BaseTest {

    @Test(priority = 3, description = "Create a new booking and validate response body")
    public void testCreateBooking_ValidPayload() {

        BookingPayload payload = PayloadFactory.defaultBooking();

        Response response = given()
                .spec(requestSpec)
                .body(payload)
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
                        "schemas/create_booking_schema.json"))
                .extract().response();

        int bookingId = response.jsonPath().getInt("bookingid");
        Assert.assertTrue(bookingId > 0, "Booking ID should be a positive integer");
        TestContext.setBookingId(bookingId);

        Assert.assertEquals(response.jsonPath().getString("booking.firstname"),
                payload.getFirstname());
        Assert.assertEquals(response.jsonPath().getString("booking.lastname"),
                payload.getLastname());
        Assert.assertEquals(response.jsonPath().getInt("booking.totalprice"),
                payload.getTotalprice());
        Assert.assertEquals(response.jsonPath().getBoolean("booking.depositpaid"),
                payload.isDepositpaid());
        Assert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkin"),
                payload.getBookingdates().getCheckin());
        Assert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkout"),
                payload.getBookingdates().getCheckout());
        Assert.assertEquals(response.jsonPath().getString("booking.additionalneeds"),
                payload.getAdditionalneeds());

        System.out.println("Booking Created with ID: " + bookingId);
    }

    @Test(priority = 4, description = "Create booking with missing fields returns 500")
    public void testCreateBooking_MissingFields() {

        given()
                .spec(requestSpec)
                .body("{ \"firstname\": \"John\" }")
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(500);
    }
}
