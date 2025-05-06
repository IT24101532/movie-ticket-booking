package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.User;
import com.movieticket.movie_ticket_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user") // Namespace all user-related endpoints
public class UserController {

    @Autowired
    private UserService userService;

    // Add other user-related endpoints here (e.g., profile updates)
    // Example:
    // @GetMapping("/profile")
    // public String showProfilePage() { ... }
}
