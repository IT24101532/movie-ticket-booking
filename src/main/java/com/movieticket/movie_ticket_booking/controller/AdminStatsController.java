package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.service.MovieService;
import com.movieticket.movie_ticket_booking.service.TheaterService;
import com.movieticket.movie_ticket_booking.service.BookingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AdminStatsController {
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final BookingService bookingService;

    public AdminStatsController(MovieService movieService, TheaterService theaterService, BookingService bookingService) {
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.bookingService = bookingService;
    }

    @GetMapping("/api/admin/stats")
    public Map<String, Integer> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("movies", movieService.getAllMovies().size());
        stats.put("theaters", theaterService.getAllTheaters().size());
        stats.put("bookings", bookingService.getAllBookings().size());
        return stats;
    }
}
