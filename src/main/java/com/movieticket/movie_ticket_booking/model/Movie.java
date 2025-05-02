package com.movieticket.movie_ticket_booking.model;

import java.time.LocalDate;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private LocalDate releaseDate;
    private int duration;
    private String director;
    private double price;
    private String posterUrl;

    // Constructor
    public Movie(int id, String title, String genre, LocalDate releaseDate,
                 int duration, String director, double price, String posterUrl) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.director = director;
        this.price = price;
        this.posterUrl = posterUrl;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    @Override
    public String toString() {
        return id + "," + title + "," + genre + "," + releaseDate + "," +
                duration + "," + director + "," + price + "," + posterUrl;
    }
}
