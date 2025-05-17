package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Booking;
import com.movieticket.movie_ticket_booking.model.Movie;
import com.movieticket.movie_ticket_booking.model.Theater;
import com.movieticket.movie_ticket_booking.model.User;
import com.movieticket.movie_ticket_booking.service.BookingService;
import com.movieticket.movie_ticket_booking.service.MovieService;
import com.movieticket.movie_ticket_booking.service.TheaterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final MovieService movieService;
    private final TheaterService theaterService;

    public BookingController(BookingService bookingService, MovieService movieService, TheaterService theaterService) {
        this.bookingService = bookingService;
        this.movieService = movieService;
        this.theaterService = theaterService;
    }

    // DTO for booking history
    public static class BookingHistoryDTO {
        public String bookingId;
        public String movieTitle;
        public String theaterName;
        public String showDateTime;
        public String seats;
        public String status;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<BookingHistoryDTO>> getUserBookings(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) return ResponseEntity.status(401).build();

            Map<String, Movie> movieMap = new HashMap<>();
            for (Movie m : movieService.getAllMovies()) movieMap.put(m.getId(), m);
            Map<String, Theater> theaterMap = new HashMap<>();
            for (Theater t : theaterService.getAllTheaters()) theaterMap.put(t.getId(), t);

            List<Booking> bookings = bookingService.getBookingsByUser(user.getId());
            List<BookingHistoryDTO> dtoList = new ArrayList<>();
            for (Booking b : bookings) {
                BookingHistoryDTO dto = new BookingHistoryDTO();
                dto.bookingId = b.getId();
                dto.movieTitle = movieMap.containsKey(b.getMovieId()) ? movieMap.get(b.getMovieId()).getTitle() : b.getMovieId();
                dto.theaterName = theaterMap.containsKey(b.getTheaterId()) ? theaterMap.get(b.getTheaterId()).getName() : b.getTheaterId();
                dto.showDateTime = formatShowDateTime(b.getShowDateTime());
                dto.seats = b.getSeatNumbers();
                dto.status = isPast(b.getShowDateTime()) ? "PAST" : "UPCOMING";
                dtoList.add(dto);
            }
            dtoList.sort(Comparator.comparing((BookingHistoryDTO d) -> d.showDateTime).reversed());
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    private String formatShowDateTime(String showDateTime) {
        try {
            String normalized = showDateTime.replace("T", " ");
            LocalDateTime dt = LocalDateTime.parse(normalized.split("\\.")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy - h:mm a");
            return dt.format(formatter);
        } catch (Exception e) {
            return showDateTime;
        }
    }

    private boolean isPast(String showDateTime) {
        try {
            String normalized = showDateTime.replace("T", " ");
            LocalDateTime dt = LocalDateTime.parse(normalized.split("\\.")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return dt.isBefore(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/all-occupied-seats")
    @ResponseBody
    public ResponseEntity<List<String>> getAllTimeOccupiedSeats(
            @RequestParam String movieId,
            @RequestParam String theaterId
    ) {
        try {
            return ResponseEntity.ok(bookingService.getAllTimeOccupiedSeats(movieId, theaterId));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/seats")
    @ResponseBody
    public ResponseEntity<List<String>> getOccupiedSeats(
            @RequestParam String movieId,
            @RequestParam String theaterId,
            @RequestParam String showtime
    ) {
        try {
            return ResponseEntity.ok(bookingService.getOccupiedSeats(movieId, theaterId, showtime));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/admin/all")
    @ResponseBody
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingsForAdmin(HttpSession session) {
        Object admin = session.getAttribute("admin");
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }
        try {
            Map<String, Movie> movieMap = new HashMap<>();
            for (Movie m : movieService.getAllMovies()) movieMap.put(m.getId(), m);
            Map<String, Theater> theaterMap = new HashMap<>();
            for (Theater t : theaterService.getAllTheaters()) theaterMap.put(t.getId(), t);

            List<Booking> bookings = bookingService.getAllBookings();
            List<BookingHistoryDTO> dtoList = new ArrayList<>();
            for (Booking b : bookings) {
                BookingHistoryDTO dto = new BookingHistoryDTO();
                dto.bookingId = b.getId();
                dto.movieTitle = movieMap.containsKey(b.getMovieId()) ? movieMap.get(b.getMovieId()).getTitle() : b.getMovieId();
                dto.theaterName = theaterMap.containsKey(b.getTheaterId()) ? theaterMap.get(b.getTheaterId()).getName() : b.getTheaterId();
                dto.showDateTime = formatShowDateTime(b.getShowDateTime());
                dto.seats = b.getSeatNumbers();
                dto.status = isPast(b.getShowDateTime()) ? "PAST" : "UPCOMING";
                dtoList.add(dto);
            }
            dtoList.sort(Comparator.comparing((BookingHistoryDTO d) -> d.showDateTime).reversed());
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


}
