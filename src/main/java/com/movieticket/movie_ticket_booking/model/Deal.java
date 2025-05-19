package com.movieticket.movie_ticket_booking.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deal {
    private String id;
    private String title;
    private String code;
    private String description;
    private double discountPercentage;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private String applicableTheaters; // Comma-separated theater IDs
    private boolean isActive;

    // Default constructor
    public Deal() {
        this.id = String.valueOf(System.currentTimeMillis());
        this.isActive = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public String getApplicableTheaters() {
        return applicableTheaters;
    }

    public void setApplicableTheaters(String applicableTheaters) {
        this.applicableTheaters = applicableTheaters;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}