package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Movie;
import com.movieticket.movie_ticket_booking.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // API: Get all movies (optionally sorted)
    @GetMapping("/api/movies")
    @ResponseBody
    public List<Movie> getMoviesApi(
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction
    ) {
        if ("releaseDate".equals(sort)) {
            boolean newestFirst = "desc".equalsIgnoreCase(direction);
            return movieService.getAllMoviesSortedByReleaseDate(newestFirst);
        } else {
            return movieService.getAllMovies();
        }
    }

    // API: Get a single movie by ID (for movie details page)
    @GetMapping("/api/movies/{id}")
    @ResponseBody
    public Movie getMovieById(@PathVariable String id) throws IOException {
        return movieService.getMovieById(id);
    }

    @GetMapping("/movies")
    public String showMovies() {
        return "movies";
    }

    @GetMapping("/movie-details")
    public String showMovieDetails(@RequestParam String id, Model model) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            return "redirect:/movies";
        }
        model.addAttribute("movie", movie);
        return "movie-details";
    }

    @DeleteMapping("/api/movies/{movieId}")
    @ResponseBody
    public boolean deleteMovie(@PathVariable String movieId) throws IOException {
        return movieService.deleteMovie(movieId);
    }
}