package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.service.MovieService;
import com.movieticket.movie_ticket_booking.service.TheaterService;
import com.movieticket.movie_ticket_booking.service.ShowtimeService;
import com.movieticket.movie_ticket_booking.service.BookingService;
import com.movieticket.movie_ticket_booking.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final ShowtimeService showtimeService;
    private final BookingService bookingService;
    private final ActivityService activityService;

    public AdminController(MovieService movieService,
                           TheaterService theaterService,
                           ShowtimeService showtimeService,
                           BookingService bookingService,
                           ActivityService activityService) {
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.showtimeService = showtimeService;
        this.bookingService = bookingService;
        this.activityService = activityService;
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        // Get counts from service classes
        int movieCount = movieService.getAllMovies().size();
        int theaterCount = theaterService.getAllTheaters().size();
        int showtimeCount = showtimeService.getAllShowtimes().size();
        int bookingCount = bookingService.getAllBookings().size();

        // Add counts to model
        model.addAttribute("movieCount", movieCount);
        model.addAttribute("theaterCount", theaterCount);
        model.addAttribute("showtimeCount", showtimeCount);
        model.addAttribute("bookingCount", bookingCount);

        // Get recent activities
        model.addAttribute("activities", activityService.getRecentActivities(5));

        return "admin";
    }
}
