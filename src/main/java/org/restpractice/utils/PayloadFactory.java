package org.restpractice.utils;

import org.restpractice.payloads.BookingPayload;
import org.restpractice.payloads.BookingPayload.BookingDates;

public class PayloadFactory {

    public static BookingPayload defaultBooking() {
        BookingDates dates = new BookingDates("2025-01-15", "2025-01-20");
        return new BookingPayload("James", "Brown", 250, true, dates, "Breakfast");
    }

    public static BookingPayload updatedBooking() {
        BookingDates dates = new BookingDates("2025-03-01", "2025-03-07");
        return new BookingPayload("Jane", "Smith", 400, false, dates, "Lunch and Dinner");
    }

    public static BookingPayload partialBooking() {
        BookingPayload payload = new BookingPayload();
        payload.setFirstname("UpdatedFirstName");
        payload.setLastname("UpdatedLastName");
        return payload;
    }

    public static String authCredentials() {
        return "{ \"username\": \"admin\", \"password\": \"password123\" }";
    }
}
