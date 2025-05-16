package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Booking;
import com.movieticket.movie_ticket_booking.model.Movie;
import com.movieticket.movie_ticket_booking.model.Theater;
import com.movieticket.movie_ticket_booking.model.User;
import com.movieticket.movie_ticket_booking.service.BookingService;
import com.movieticket.movie_ticket_booking.service.MovieService;
import com.movieticket.movie_ticket_booking.service.TheaterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class BookingController {
    private final BookingService bookingService;
    private final MovieService movieService;
    private final TheaterService theaterService;

    public BookingController(BookingService bookingService, MovieService movieService, TheaterService theaterService) {
        this.bookingService = bookingService;
        this.movieService = movieService;
        this.theaterService = theaterService;
    }

    // ... existing DTO and other methods ...

    // NEW: Endpoint for all-time occupied seats (ignores showtime)
    @GetMapping("/api/bookings/all-occupied-seats")
    @ResponseBody
    public List<String> getAllTimeOccupiedSeats(
            @RequestParam String movieId,
            @RequestParam String theaterId
    ) throws IOException {
        return bookingService.getAllTimeOccupiedSeats(movieId, theaterId);
    }

    // Existing endpoint for specific showtime
    @GetMapping("/api/bookings/seats")
    @ResponseBody
    public List<String> getOccupiedSeats(
            @RequestParam String movieId,
            @RequestParam String theaterId,
            @RequestParam String showtime
    ) throws IOException {
        return bookingService.getOccupiedSeats(movieId, theaterId, showtime);
    }

    // ... rest of your existing code ...
}
