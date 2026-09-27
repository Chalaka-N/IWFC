package com.iwfc.model;

public class Booking {
    private final String bookingId;
    private final String memberId;
    private final String sessionId;

    public Booking(String bookingId, String memberId, String sessionId) {
        this.bookingId = bookingId;
        this.memberId = memberId;
        this.sessionId = sessionId;
    }

    public String getBookingId() { return bookingId; }
    public String getMemberId() { return memberId; }
    public String getSessionId() { return sessionId; }
}
