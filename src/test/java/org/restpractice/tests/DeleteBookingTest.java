package org.restpractice.tests;

import org.restpractice.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DeleteBookingTest extends BaseTest {
    private int bookingId;

    @BeforeClass
    public void setup() {
        ensureAuthSpec();
        bookingId = ensureActiveBooking();
    }

    @Test(priority = 14, description = "Delete without auth token returns 403")
    public void testDeleteBooking_NoAuth() {

        given()
                .spec(requestSpec)
                .pathParam("id", bookingId)
                .when()
                .delete("/booking/{id}")
                .then()
                .log().all()
                .statusCode(403);
    }

    @Test(priority = 15, description = "Delete with valid auth token returns 201")
    public void testDeleteBooking_ValidAuth() {

        Assert.assertTrue(bookingId > 0);

        given()
                .spec(authRequestSpec)
                .pathParam("id", bookingId)
                .when()
                .delete("/booking/{id}")
                .then()
                .log().all()
                .statusCode(201);

        System.out.println("Booking deleted for ID: " + bookingId);
    }

    @Test(priority = 16, description = "After deletion, GET returns 404")
    public void testDeleteBooking_VerifyDeletion() {

        given()
                .spec(requestSpec)
                .pathParam("id", bookingId)
                .when()
                .get("/booking/{id}")
                .then()
                .log().all()
                .statusCode(404);

        System.out.println("Deletion verified — 404 confirmed");
    }

    @Test(priority = 17, description = "Delete non-existent booking returns 405")
    public void testDeleteBooking_InvalidId() {

        given()
                .spec(authRequestSpec)
                .pathParam("id", 999999)
                .when()
                .delete("/booking/{id}")
                .then()
                .log().all()
                .statusCode(405);
    }
}
