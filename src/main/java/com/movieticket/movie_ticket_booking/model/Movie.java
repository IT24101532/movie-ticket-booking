package com.movieticket.movie_ticket_booking.model;

import java.time.LocalDate;

public class Movie {
    private String title;
    private String genre;
    private String language;
    private LocalDate releaseDate;
    private String description;

    public Movie() {}

    public Movie(String title, String genre, String language, LocalDate releaseDate, String description) {
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.releaseDate = releaseDate;
        this.description = description;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
