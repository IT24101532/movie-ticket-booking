package com.movieticket.movie_ticket_booking.model;

import java.util.ArrayList;
import java.util.List;

public class Theater {
    private String id;
    private String name;
    private String location;
    private int totalSeats;
    private int seatRows;
    private List<String> movieIds = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public int getSeatRows() { return seatRows; }
    public void setSeatRows(int seatRows) { this.seatRows = seatRows; }
    public List<String> getMovieIds() { return movieIds; }
    public void setMovieIds(List<String> movieIds) { this.movieIds = movieIds; }
}