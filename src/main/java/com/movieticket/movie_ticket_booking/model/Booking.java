package com.movieticket.movie_ticket_booking.model;

import java.time.LocalDateTime;

public class Booking {
    private String id;
    private String userId;
    private String showtimeId;
    private LocalDateTime bookingTime;
    private String seatNumbers;
    private double totalAmount;
    private String movieId;
    private String theaterId;

    // --- Standard getters and setters ---

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShowtimeId() {
        return showtimeId;
    }
    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }
    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }

    // --- Aliases for compatibility with controller/service usage ---

    // For showDateTime (string version)
    public String getShowDateTime() {
        return bookingTime != null ? bookingTime.toString() : "";
    }
    public void setShowDateTime(String showDateTime) {
        // Optionally parse and set bookingTime from string
        if (showDateTime != null && !showDateTime.isEmpty()) {
            this.bookingTime = LocalDateTime.parse(showDateTime.split("\\.")[0]);
        }
    }

    // For seats
    public String getSeats() {
        return seatNumbers;
    }
    public void setSeats(String seats) {
        this.seatNumbers = seats;
    }

    // For total
    public double getTotal() {
        return totalAmount;
    }
    public void setTotal(double total) {
        this.totalAmount = total;
    }
}
