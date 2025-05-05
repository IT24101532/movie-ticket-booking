package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Theater;
import com.movieticket.movie_ticket_booking.service.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class TheaterController {
    private final TheaterService theaterService = new TheaterService();

    @GetMapping("/theaters")
    public String showTheatersPage() {
        return "theaters.html";
    }

    @GetMapping("/api/theaters")
    @ResponseBody
    public List<Theater> getTheaters() throws IOException {
        return theaterService.getAllTheaters();
    }
}
