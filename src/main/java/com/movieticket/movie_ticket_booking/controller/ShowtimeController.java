package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    // Assign a showtime to a movie-theater pair (ADMIN)
    @PostMapping("/admin/assign-showtime")
    public String assignShowtime(
            @RequestParam String movieId,
            @RequestParam String theaterId,
            @RequestParam String showTime
    ) {
        try {
            showtimeService.assignShowtime(movieId, theaterId, showTime);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed: " + e.getMessage();
        }
    }

    // Fetch all showtimes for a movie-theater pair (legacy, not used by new UI)
    @GetMapping("/api/showtimes")
    public List<String> getShowtimes(
            @RequestParam String movieId,
            @RequestParam String theaterId
    ) throws IOException {
        return showtimeService.getShowtimes(movieId, theaterId);
    }

    // NEW: Fetch all available dates for a movie-theater pair
    @GetMapping("/api/showdates")
    public List<String> getShowDates(
            @RequestParam String movieId,
            @RequestParam String theaterId
    ) throws IOException {
        return showtimeService.getShowDates(movieId, theaterId);
    }

    // NEW: Fetch all available times for a given movie-theater-date
    @GetMapping(value = "/api/showtimes", params = {"movieId", "theaterId", "date"})
    public List<String> getShowTimesForDate(
            @RequestParam String movieId,
            @RequestParam String theaterId,
            @RequestParam String date
    ) throws IOException {
        return showtimeService.getShowTimesForDate(movieId, theaterId, date);
    }
}