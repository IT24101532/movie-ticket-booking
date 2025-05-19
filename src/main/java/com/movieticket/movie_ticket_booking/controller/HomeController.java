package com.movieticket.movie_ticket_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage() {
        return "index.html";
    }

    // Changed mapping from "/admin" to "/admin-login" to avoid conflict
    @GetMapping("/admin-page")
    public String showAdminPage() {
        // You might want to add authentication check here
        return "redirect:/admin/login.html";
    }

}
