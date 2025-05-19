package com.movieticket.movie_ticket_booking.model;

import java.time.LocalDateTime;

public class Payment {
    private String paymentId;
    private String bookingId;
    private String userId;
    private double amount;
    private String method; // e.g., "Credit Card", "UPI", etc.
    private LocalDateTime paymentTime;
    private String status; // e.g., "SUCCESS", "FAILED", "REFUNDED"

    public Payment() {}

    public Payment(String paymentId, String bookingId, String userId, double amount, String method, LocalDateTime paymentTime, String status) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.method = method;
        this.paymentTime = paymentTime;
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}