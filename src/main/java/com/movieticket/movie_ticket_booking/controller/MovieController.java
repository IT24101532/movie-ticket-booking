package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Movie;
import com.movieticket.movie_ticket_booking.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class MovieController {
    private final MovieService movieService = new MovieService();

    @GetMapping("/movies")
    public String showMoviesPage() {
        return "movies.html";
    }

    @GetMapping("/api/movies")
    @ResponseBody
    public List<Movie> getMovies() throws IOException {
        List<Movie> movies = movieService.getAllMovies();
        return movieService.sortMoviesByReleaseDate(movies);
    }
}
