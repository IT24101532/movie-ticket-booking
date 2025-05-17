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
            // Optionally fetch latest from DB/service if needed
            // return userService.getUserById(user.getId());
            return user;
        }
        // Not logged in, return empty user or handle unauthorized as needed
        return new User();
    }

    // PUT /api/user/profile - update the logged-in user's profile
    @PutMapping("/profile")
    public User updateProfile(@RequestBody User updatedUser, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            // Not logged in, return empty or handle unauthorized
            return new User();
        }

        // Update fields using your User model's methods
        sessionUser.setFirstName(updatedUser.getFirstName());
        sessionUser.setLastName(updatedUser.getLastName());
        sessionUser.setEmail(updatedUser.getEmail());
        sessionUser.setMobile(updatedUser.getMobile());
        sessionUser.setGender(updatedUser.getGender());
        sessionUser.setNic(updatedUser.getNic());
        sessionUser.setAddress(updatedUser.getAddress());
        // Do NOT update password or role here unless intended

        // If you have a persistence mechanism, save the updated user here.
        // For example: userService.saveOrUpdate(sessionUser);

        // Update the session with the new user info
        session.setAttribute("user", sessionUser);

        return sessionUser;
    }
}
