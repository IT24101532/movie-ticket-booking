package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.User;
import com.movieticket.movie_ticket_booking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserProfileRestController {

    @Autowired
    private UserService userService;

    // GET /api/user/profile - returns all user details for the logged-in user
    @GetMapping("/profile")
    public User getProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Optionally, fetch latest from DB/service if needed:
            // return userService.getUserById(user.getId());
            return user;
        }
        // Not logged in, return empty user or handle unauthorized as needed
        return new User();
    }
}
