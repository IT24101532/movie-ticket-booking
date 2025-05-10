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

    // DTO for booking history card
    public static class BookingHistoryDTO {
        public String bookingId;
        public String movieTitle;
        public String theaterName;
        public String showDateTime;
        public String seats;
        public String total;
        public String status; // "UPCOMING" or "PAST"
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
    public List<BookingHistoryDTO> getUserBookings(HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user == null) return List.of();

        // Load all movies and theaters into maps for quick lookup
        Map<String, Movie> movieMap = new HashMap<>();
        for (Movie m : movieService.getAllMovies()) {
            movieMap.put(m.getId(), m);
        }
        Map<String, Theater> theaterMap = new HashMap<>();
        for (Theater t : theaterService.getAllTheaters()) {
            theaterMap.put(t.getId(), t);
        }

        List<Booking> bookings = bookingService.getBookingsByUser(user.getId());
        List<BookingHistoryDTO> dtoList = new ArrayList<>();

        for (Booking b : bookings) {
            BookingHistoryDTO dto = new BookingHistoryDTO();
            dto.bookingId = b.getId();
            // Get movie and theater names (fallback to ID if missing)
            dto.movieTitle = movieMap.containsKey(b.getMovieId()) ? movieMap.get(b.getMovieId()).getTitle() : b.getMovieId();
            dto.theaterName = theaterMap.containsKey(b.getTheaterId()) ? theaterMap.get(b.getTheaterId()).getName() : b.getTheaterId();
            dto.showDateTime = formatShowDateTime(b.getShowDateTime());
            dto.seats = b.getSeats();
            dto.total = String.valueOf(b.getTotal());
            dto.status = isPast(b.getShowDateTime()) ? "PAST" : "UPCOMING";
            dtoList.add(dto);
        }
        // Optionally sort by showDateTime descending (most recent first)
        dtoList.sort(Comparator.comparing((BookingHistoryDTO d) -> d.showDateTime).reversed());
        return dtoList;
    }

    // Helper to format showDateTime for display
    private String formatShowDateTime(String showDateTime) {
        try {
            // Assuming showDateTime is in ISO_LOCAL_DATE_TIME format
            LocalDateTime dt = LocalDateTime.parse(showDateTime.split("\\.")[0]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy - h:mm a");
            return dt.format(formatter);
        } catch (Exception e) {
            return showDateTime;
        }
    }

    // Helper to determine if a show is in the past
    private boolean isPast(String showDateTime) {
        try {
            LocalDateTime dt = LocalDateTime.parse(showDateTime.split("\\.")[0]);
            return dt.isBefore(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }

    // ... (other endpoints, e.g. booking confirmation, cancel, etc. remain unchanged) ...
}
