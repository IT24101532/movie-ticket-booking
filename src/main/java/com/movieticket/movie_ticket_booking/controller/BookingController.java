package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Booking;
import com.movieticket.movie_ticket_booking.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class BookingController {
    private final BookingService bookingService = new BookingService();

    // Page mappings
    @GetMapping("/select-seats")
    public String showSelectSeatsPage() {
        return "select-seats.html";
    }

    @GetMapping("/booking-confirmation")
    public String showBookingConfirmationPage() {
        return "booking-confirmation.html";
    }

    @GetMapping("/booking-history")
    public String showBookingHistoryPage() {
        return "booking-history.html";
    }

    // API endpoints
    @PostMapping("/api/bookings")
    @ResponseBody
    public Booking createBooking(@RequestBody Booking booking) throws IOException {
        return bookingService.processBooking(booking);
    }

    @GetMapping("/api/bookings")
    @ResponseBody
    public List<Booking> getUserBookings(@RequestParam String userId) throws IOException {
        return bookingService.getBookingsByUser(userId);
    }

    @GetMapping("/api/bookings/{bookingId}")
    @ResponseBody
    public Booking getBooking(@PathVariable String bookingId) throws IOException {
        return bookingService.getBookingById(bookingId);
    }

    @DeleteMapping("/api/bookings/{bookingId}")
    @ResponseBody
    public boolean cancelBooking(@PathVariable String bookingId, @RequestParam String userId) throws IOException {
        return bookingService.cancelBooking(bookingId, userId);
    }
}
