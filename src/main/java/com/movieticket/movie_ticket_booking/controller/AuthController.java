package com.movieticket.movie_ticket_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/admin-login")
    public String showAdminLoginPage() {
        return "admin-login.html";
    }

    @PostMapping("/admin-auth")
    public String adminAuth(@RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "adminpass".equals(password)) {
            return "redirect:/admin-home.html";
        } else {
            return "redirect:/admin-login.html?error";
        }
    }

    // Add other authentication-related methods here
}
