package org.restpractice.payloads;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingPayload {

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("totalprice")
    private int totalprice;

    @JsonProperty("depositpaid")
    private boolean depositpaid;

    @JsonProperty("bookingdates")
    private BookingDates bookingdates;

    @JsonProperty("additionalneeds")
    private String additionalneeds;

    public BookingPayload() {}

    public BookingPayload(String firstname, String lastname, int totalprice,
                          boolean depositpaid, BookingDates bookingdates,
                          String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;
    }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public int getTotalprice() { return totalprice; }
    public void setTotalprice(int totalprice) { this.totalprice = totalprice; }

    public boolean isDepositpaid() { return depositpaid; }
    public void setDepositpaid(boolean depositpaid) { this.depositpaid = depositpaid; }

    public BookingDates getBookingdates() { return bookingdates; }
    public void setBookingdates(BookingDates bookingdates) { this.bookingdates = bookingdates; }

    public String getAdditionalneeds() { return additionalneeds; }
    public void setAdditionalneeds(String additionalneeds) { this.additionalneeds = additionalneeds; }

    public static class BookingDates {

        @JsonProperty("checkin")
        private String checkin;

        @JsonProperty("checkout")
        private String checkout;

        public BookingDates() {}

        public BookingDates(String checkin, String checkout) {
            this.checkin = checkin;
            this.checkout = checkout;
        }

        public String getCheckin() { return checkin; }
        public void setCheckin(String checkin) { this.checkin = checkin; }

        public String getCheckout() { return checkout; }
        public void setCheckout(String checkout) { this.checkout = checkout; }
    }
}
