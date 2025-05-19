package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Movie;
import com.movieticket.movie_ticket_booking.model.Theater;
import com.movieticket.movie_ticket_booking.service.MovieService;
import com.movieticket.movie_ticket_booking.service.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class TheaterController {
    private final TheaterService theaterService;
    private final MovieService movieService;

    public TheaterController(TheaterService theaterService, MovieService movieService) {
        this.theaterService = theaterService;
        this.movieService = movieService;
    }

    // DTO for assignments
    public static class MovieAssignmentDTO {
        public String theaterName;
        public String movieTitle;
        public String theaterId;
        public String movieId;

        public MovieAssignmentDTO(String theaterName, String movieTitle, String theaterId, String movieId) {
            this.theaterName = theaterName;
            this.movieTitle = movieTitle;
            this.theaterId = theaterId;
            this.movieId = movieId;
        }
    }

    // Endpoint: List all movie-theater assignments (for admin/showtime UI)
    @GetMapping("/api/movie-assignments")
    @ResponseBody
    public List<MovieAssignmentDTO> getMovieAssignments() throws IOException {
        List<MovieAssignmentDTO> assignments = new java.util.ArrayList<>();
        for (Theater theater : theaterService.getAllTheaters()) {
            for (String movieId : theater.getMovieIds()) {
                Movie movie = movieService.getMovieById(movieId);
                if (movie != null) {
                    assignments.add(new MovieAssignmentDTO(
                            theater.getName(),
                            movie.getTitle(),
                            theater.getId(),
                            movie.getId()
                    ));
                }
            }
        }
        assignments.sort(java.util.Comparator.comparing((MovieAssignmentDTO a) -> a.theaterName)
                .thenComparing(a -> a.movieTitle));
        return assignments;
    }

    // Endpoint: List all theaters showing a given movie (for user movie details page)
    @GetMapping("/api/theaters")
    @ResponseBody
    public List<Theater> getTheatersByMovie(@RequestParam String movieId) throws IOException {
        return theaterService.getTheatersByMovieId(movieId);
    }

    // --- Other endpoints you may already have ---

    // Get all theaters (admin use)
    @GetMapping("/api/all-theaters")
    @ResponseBody
    public List<Theater> getAllTheaters() throws IOException {
        return theaterService.getAllTheaters();
    }

    // Get a specific theater by ID
    @GetMapping("/api/theaters/{theaterId}")
    @ResponseBody
    public Theater getTheaterById(@PathVariable String theaterId) throws IOException {
        return theaterService.getTheaterById(theaterId);
    }

    // Admin: Add a new theater
    @PostMapping("/admin/add-theater")
    public String addTheater(
            @RequestParam String name,
            @RequestParam String location,
            @RequestParam int totalSeats,
            @RequestParam int seatRows
    ) throws IOException {
        Theater theater = new Theater();
        theater.setName(name);
        theater.setLocation(location);
        theater.setTotalSeats(totalSeats);
        theater.setSeatRows(seatRows);
        theaterService.addTheater(theater);
        return "redirect:/admin-theaters.html";
    }

    // Admin: Delete a theater
    @PostMapping("/admin/delete-theater")
    public String deleteTheater(@RequestParam String theaterId) throws IOException {
        theaterService.deleteTheater(theaterId);
        return "redirect:/admin-theaters.html";
    }

    // Admin: Assign a movie to a theater
    @PostMapping("/admin/assign-movie")
    public String assignMovieToTheater(
            @RequestParam String theaterId,
            @RequestParam String movieId
    ) throws IOException {
        theaterService.addMovieToTheater(theaterId, movieId);
        return "redirect:/admin-theaters.html";
    }
}