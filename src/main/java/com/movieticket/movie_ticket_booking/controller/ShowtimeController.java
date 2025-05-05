package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Showtime;
import com.movieticket.movie_ticket_booking.service.ShowtimeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class ShowtimeController {
    private final ShowtimeService showtimeService = new ShowtimeService();

    @GetMapping("/showtimes")
    public String showShowtimesPage() {
        return "showtimes.html";
    }

    @GetMapping("/api/showtimes")
    @ResponseBody
    public List<Showtime> getShowtimes() throws IOException {
        return showtimeService.getAllShowtimes();
    }
}
