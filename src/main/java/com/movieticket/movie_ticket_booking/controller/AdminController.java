package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Movie;
import com.movieticket.movie_ticket_booking.service.BookingService;
import com.movieticket.movie_ticket_booking.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MovieService movieService;
    private final BookingService bookingService; // NEW: Added BookingService dependency

    // Updated constructor to include BookingService
    public AdminController(MovieService movieService, BookingService bookingService) {
        this.movieService = movieService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String adminDashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("movieCount", movieService.getAllMovies().size());
        } catch (Exception e) {
            model.addAttribute("error", "Error loading movies: " + e.getMessage());
        }
        return "admin-dashboard";
    }

    // NEW: Endpoint to process next booking in queue
    @PostMapping("/process-next-booking")
    public String processNextBooking(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            bookingService.processNextInQueue();
        } catch (Exception e) {
            // Handle error (you could add to model if needed)
            System.err.println("Error processing next booking: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/add-movie")
    public String addMovie(
            @RequestParam String title,
            @RequestParam String genre,
            @RequestParam String language,
            @RequestParam String releaseDate,
            @RequestParam String showTime,
            @RequestParam String theater,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam("imageFile") MultipartFile imageFile,
            HttpSession session,
            Model model) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setLanguage(language);
            movie.setReleaseDate(LocalDate.parse(releaseDate));
            movie.setShowTime(LocalTime.parse(showTime));
            movie.setTheater(theater);
            movie.setDescription(description);
            movie.setPrice(price);

            movieService.saveMovie(movie, imageFile);
            return "redirect:/admin";

        } catch (Exception e) {
            model.addAttribute("error", "Error adding movie: " + e.getMessage());
            return "admin-dashboard";
        }
    }

    @PostMapping("/delete-movie/{id}")
    public String deleteMovie(
            @PathVariable String id,
            HttpSession session,
            Model model) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            movieService.deleteMovie(id);
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting movie: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
