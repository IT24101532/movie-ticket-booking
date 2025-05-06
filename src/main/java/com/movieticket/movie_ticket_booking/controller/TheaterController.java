package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Theater;
import com.movieticket.movie_ticket_booking.service.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class TheaterController {
    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    // ------------------- NEW ENDPOINT ADDED HERE -------------------
    @GetMapping("/api/theaters/{theaterId}")
    @ResponseBody
    public Theater getTheaterById(@PathVariable String theaterId) throws IOException {
        return theaterService.getTheaterById(theaterId);
    }
    // ---------------------------------------------------------------

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

    @PostMapping("/admin/delete-theater")
    public String deleteTheater(@RequestParam String theaterId) throws IOException {
        theaterService.deleteTheater(theaterId);
        return "redirect:/admin-theaters.html";
    }

    @PostMapping("/admin/assign-movie")
    public String assignMovieToTheater(
            @RequestParam String theaterId,
            @RequestParam String movieId
    ) throws IOException {
        theaterService.addMovieToTheater(theaterId, movieId);
        return "redirect:/admin-theaters.html";
    }

    @GetMapping("/api/theaters")
    @ResponseBody
    public List<Theater> getTheatersByMovie(@RequestParam String movieId) throws IOException {
        return theaterService.getTheatersByMovieId(movieId);
    }

    @GetMapping("/api/all-theaters")
    @ResponseBody
    public List<Theater> getAllTheaters() throws IOException {
        return theaterService.getAllTheaters();
    }
}
