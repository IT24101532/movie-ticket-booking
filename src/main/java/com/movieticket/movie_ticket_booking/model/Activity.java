package com.movieticket.movie_ticket_booking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Activity {
    private LocalDateTime timestamp;
    private String action;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
}
