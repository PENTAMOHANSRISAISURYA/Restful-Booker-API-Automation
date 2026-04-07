package org.restpractice.tests;

import org.restpractice.base.BaseTest;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetBookingTest extends BaseTest {

    @Test(priority = 5, description = "Get all bookings — returns non-empty list of IDs")
    public void testGetAllBookings() {

        given()
                .spec(requestSpec)
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", not(empty()))
                .body("bookingid", everyItem(greaterThan(0)));
    }

    @Test(priority = 6, description = "Get booking by ID — validate all fields")
    public void testGetBookingById_ValidId() {

        int bookingId = ensureActiveBooking();
        Assert.assertTrue(bookingId > 0, "Booking ID must be set from CreateBookingTest");

        Response response = given()
                .spec(requestSpec)
                .pathParam("id", bookingId)
                .when()
                .get("/booking/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
                        "schemas/get_booking_schema.json"))
                .extract().response();

        Assert.assertEquals(response.jsonPath().getString("firstname"), "James");
        Assert.assertEquals(response.jsonPath().getString("lastname"), "Brown");
        Assert.assertEquals(response.jsonPath().getInt("totalprice"), 250);
        Assert.assertTrue(response.jsonPath().getBoolean("depositpaid"));
        Assert.assertEquals(response.jsonPath().getString("additionalneeds"), "Breakfast");

        System.out.println("Booking retrieved for ID: " + bookingId);
    }

    @Test(priority = 7, description = "Non-existent booking ID returns 404")
    public void testGetBookingById_InvalidId() {

        given()
                .spec(requestSpec)
                .pathParam("id", 999999)
                .when()
                .get("/booking/{id}")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test(priority = 8, description = "Filter bookings by firstname query param")
    public void testGetBookings_FilterByFirstname() {

        given()
                .spec(requestSpec)
                .queryParam("firstname", "James")
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test(priority = 9, description = "Filter bookings by check-in date")
    public void testGetBookings_FilterByCheckinDate() {

        given()
                .spec(requestSpec)
                .queryParam("checkin", "2025-01-01")
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200);
    }
}
