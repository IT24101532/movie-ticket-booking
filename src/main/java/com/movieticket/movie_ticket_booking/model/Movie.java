package com.movieticket.movie_ticket_booking.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private String language;
    private LocalDate releaseDate;
    private LocalTime showTime;
    private String theater;
    private String description;
    private String imagePath;
    private Double price;
    private String eventType;

    // Default constructor
    public Movie() {
        this.showTime = LocalTime.of(18, 0); // Default 6:00 PM
        this.theater = "Cinema Hall";
        this.eventType = "Movie";
        this.price = 300.0;
    }

    // All getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public LocalTime getShowTime() { return showTime; }
    public void setShowTime(LocalTime showTime) { this.showTime = showTime; }

    public String getTheater() { return theater; }
    public void setTheater(String theater) { this.theater = theater; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

}