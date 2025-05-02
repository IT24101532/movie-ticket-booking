package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Movie;
import com.movieticket.movie_ticket_booking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

        import java.time.LocalDate;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Browse movies page
    @GetMapping("/movies")
    public String browseMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies/browse";
    }


    // Movie details page
    @GetMapping("/movies/{id}")
    public String movieDetails(@PathVariable int id, Model model) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            return "redirect:/movies";
        }
        model.addAttribute("movie", movie);
        return "movies/details";
    }

    // Search functionality
    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam String keyword, Model model) {
        List<Movie> results = movieService.searchMovies(keyword);
        model.addAttribute("movies", results);
        model.addAttribute("keyword", keyword);
        return "movies/browse";
    }

    // Filter by genre
    @GetMapping("/movies/genre/{genre}")
    public String filterByGenre(@PathVariable String genre, Model model) {
        List<Movie> results = movieService.filterByGenre(genre);
        model.addAttribute("movies", results);
        model.addAttribute("selectedGenre", genre);
        return "movies/browse";
    }

    // Admin section - Movie management page
    @GetMapping("/admin/movies")
    public String manageMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "admin/manage-movies";
    }

    // Add movie form
    @GetMapping("/admin/movies/add")
    public String showAddMovieForm() {
        return "admin/add-movie";
    }

    // Process add movie
    @PostMapping("/admin/movies/add")
    public String addMovie(
            @RequestParam String title,
            @RequestParam String genre,
            @RequestParam String releaseDate,
            @RequestParam int duration,
            @RequestParam String director,
            @RequestParam double price,
            @RequestParam String posterUrl) {

        Movie newMovie = new Movie(
                0, // ID will be set in service
                title,
                genre,
                LocalDate.parse(releaseDate),
                duration,
                director,
                price,
                posterUrl
        );

        movieService.addMovie(newMovie);
        return "redirect:/admin/movies";
    }

    // Edit movie form
    @GetMapping("/admin/movies/edit/{id}")
    public String showEditMovieForm(@PathVariable int id, Model model) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            return "redirect:/admin/movies";
        }
        model.addAttribute("movie", movie);
        return "admin/edit-movie";
    }

    // Process edit movie
    @PostMapping("/admin/movies/edit/{id}")
    public String updateMovie(
            @PathVariable int id,
            @RequestParam String title,
            @RequestParam String genre,
            @RequestParam String releaseDate,
            @RequestParam int duration,
            @RequestParam String director,
            @RequestParam double price,
            @RequestParam String posterUrl) {

        Movie updatedMovie = new Movie(
                id,
                title,
                genre,
                LocalDate.parse(releaseDate),
                duration,
                director,
                price,
                posterUrl
        );

        movieService.updateMovie(id, updatedMovie);
        return "redirect:/admin/movies";
    }

    // Delete movie
    @PostMapping("/admin/movies/delete/{id}")
    public String deleteMovie(@PathVariable int id) {
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }
}
