package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Booking;
import com.movieticket.movie_ticket_booking.model.User;
import com.movieticket.movie_ticket_booking.service.BookingService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;

@Controller
public class PaymentController {
    private final BookingService bookingService;

    public PaymentController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/api/payments")
    public void processPayment(
            @RequestParam String movieId,
            @RequestParam String theaterId,
            @RequestParam String seatNumbers,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) Double totalAmount,
            HttpSession session,
            HttpServletResponse response) throws IOException {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
            return;
        }

        try {
            Booking booking = new Booking();
            booking.setMovieId(movieId);
            booking.setTheaterId(theaterId);
            booking.setUserId(user.getId());
            booking.setSeatNumbers(seatNumbers);
            if (totalAmount != null) {
                booking.setTotalAmount(totalAmount);
            } else {
                booking.setTotalAmount(seatNumbers.split(",").length * 300);
            }

            bookingService.processBooking(booking);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage()); // 409 Conflict
        }
    }
}
