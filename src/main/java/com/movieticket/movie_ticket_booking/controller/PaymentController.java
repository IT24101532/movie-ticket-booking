package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Booking;
import com.movieticket.movie_ticket_booking.model.User;
import com.movieticket.movie_ticket_booking.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class PaymentController {
    private final BookingService bookingService;

    public PaymentController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/api/payments")
    @ResponseBody
    public ResponseEntity<?> processPayment(
            @RequestParam String movieId,
            @RequestParam String theaterId,
            @RequestParam String showDateTime,
            @RequestParam String seatNumbers,
            @RequestParam double totalAmount,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("error", "Not logged in");
            return ResponseEntity.status(401).body(response);
        }
        try {
            Booking booking = new Booking();
            booking.setId(UUID.randomUUID().toString());
            booking.setMovieId(movieId);
            booking.setTheaterId(theaterId);
            booking.setShowDateTime(showDateTime);
            booking.setUserId(user.getId());
            booking.setSeatNumbers(seatNumbers);
            booking.setTotalAmount(totalAmount);

            bookingService.processBooking(booking);
            response.put("success", true);
            response.put("bookingId", booking.getId());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("error", "seat_conflict");
            response.put("message", e.getMessage());
            return ResponseEntity.status(409).body(response);
        } catch (Exception e) {
            response.put("error", "processing_error");
            response.put("message", "Failed to process payment");
            return ResponseEntity.status(500).body(response);
        }
    }
}
