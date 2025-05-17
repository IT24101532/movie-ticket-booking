package com.movieticket.movie_ticket_booking.model;

public class Booking {
    private String id;
    private String userId;
    private String movieId;
    private String theaterId;
    private String showDateTime; // Always store as string "YYYY-MM-DD HH:mm"
    private String seatNumbers;  // e.g. "A1,A2"
    private double totalAmount;

    // --- Standard getters and setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }

    public String getShowDateTime() { return showDateTime; }
    public void setShowDateTime(String showDateTime) { this.showDateTime = showDateTime; }

    public String getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(String seatNumbers) { this.seatNumbers = seatNumbers; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    // --- Aliases for compatibility (optional, but not required if you update all usage) ---
    public String getSeats() { return seatNumbers; }
    public void setSeats(String seats) { this.seatNumbers = seats; }
    public double getTotal() { return totalAmount; }
    public void setTotal(double total) { this.totalAmount = total; }
}
