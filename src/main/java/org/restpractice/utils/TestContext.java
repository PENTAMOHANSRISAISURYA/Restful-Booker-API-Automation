package org.restpractice.utils;

public class TestContext {

    private static int bookingId;
    private static String token;

    public static int getBookingId() { return bookingId; }
    public static void setBookingId(int id) { bookingId = id; }

    public static String getToken() { return token; }
    public static void setToken(String t) { token = t; }
}
