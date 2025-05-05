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

    @GetMapping("/api/movies")
    @ResponseBody
    public List<Movie> getMoviesApi() {
        return movieService.getAllMovies();
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

    @PostMapping("/admin/add-movie")
    public String addMovie(
            @RequestParam String title,
            @RequestParam String genre,
            @RequestParam String language,
            @RequestParam String releaseDate,
            @RequestParam(required = false) String showTime,
            @RequestParam(required = false) String theater,
            @RequestParam String description,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String eventType,
            @RequestParam MultipartFile movieImage,
            Model model
    ) {
        try {
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setLanguage(language);
            movie.setReleaseDate(LocalDate.parse(releaseDate));
            movie.setDescription(description);

            if (showTime != null && !showTime.isEmpty()) {
                movie.setShowTime(LocalTime.parse(showTime));
            }
            if (theater != null && !theater.isEmpty()) {
                movie.setTheater(theater);
            }
            if (price != null) {
                movie.setPrice(price);
            }
            if (eventType != null && !eventType.isEmpty()) {
                movie.setEventType(eventType);
            }

            movieService.saveMovie(movie, movieImage);
            return "redirect:/movies";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to save movie: " + e.getMessage());
            return "admin-movies";
        }
    }
}
