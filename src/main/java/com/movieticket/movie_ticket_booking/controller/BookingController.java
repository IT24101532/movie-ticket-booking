package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Booking;
import com.movieticket.movie_ticket_booking.model.Movie;
import com.movieticket.movie_ticket_booking.model.User;
import com.movieticket.movie_ticket_booking.service.BookingService;
import com.movieticket.movie_ticket_booking.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@Controller
public class BookingController {
    private final BookingService bookingService;
    private final MovieService movieService; // Inject MovieService

    // Constructor injection for both services
    public BookingController(BookingService bookingService, MovieService movieService) {
        this.bookingService = bookingService;
        this.movieService = movieService;
    }

    @GetMapping("/booking-confirmation")
    public String showBookingConfirmationPage() {
        return "booking-confirmation.html";
    }

    @GetMapping("/booking-history")
    public String showBookingHistoryPage(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "booking-history.html";
    }

    @GetMapping("/api/bookings")
    @ResponseBody
    public List<Booking> getUserBookings(HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user == null) return List.of();
        return bookingService.getBookingsByUser(user.getId());
    }

    @GetMapping("/api/bookings/seats")
    @ResponseBody
    public List<String> getOccupiedSeats(
            @RequestParam String movieId,
            @RequestParam String theaterId) throws IOException {
        return bookingService.getOccupiedSeats(movieId, theaterId);
    }

    // You can move this to a MovieController if you want
    @GetMapping("/api/movies/{id}")
    @ResponseBody
    public Movie getMovie(@PathVariable String id) throws IOException {
        return movieService.getMovieById(id);
    }

    @DeleteMapping("/api/bookings/{bookingId}")
    @ResponseBody
    public boolean cancelBooking(
            @PathVariable String bookingId,
            HttpSession session) throws IOException {

        User user = (User) session.getAttribute("user");
        if (user == null) return false;

        return bookingService.cancelBooking(bookingId, user.getId());
    }
}
