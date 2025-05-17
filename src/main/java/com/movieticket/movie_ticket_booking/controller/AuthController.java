package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.User;
import com.movieticket.movie_ticket_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login.html";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {
            User user = userService.findUserByEmailAndPassword(email, password);

            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("isLoggedIn", true);
                session.setAttribute("userName", user.getFirstName() + " " + user.getLastName());

                // Only set admin attribute if user is actually an admin
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    session.setAttribute("admin", user);
                    return "redirect:/admin-home.html";
                }
                return "redirect:/movies.html";
            } else {
                return "redirect:/login.html?error=invalid";
            }
        } catch (IOException e) {
            return "redirect:/login.html?error=system";
        }
    }

    @GetMapping("/admin-login")
    public String showAdminLoginPage() {
        return "admin-login.html";
    }

    @PostMapping("/admin-auth")
    public String adminAuth(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        try {
            User admin = userService.authenticateAdmin(email, password);
            if (admin != null) {
                // Set both attributes for admin
                session.setAttribute("user", admin);
                session.setAttribute("admin", admin);
                session.setAttribute("isLoggedIn", true);
                session.setAttribute("userName", "Admin User");
                return "redirect:/admin-home.html";
            } else {
                return "redirect:/admin-login.html?error=invalid";
            }
        } catch (Exception e) {
            return "redirect:/admin-login.html?error=system";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register.html";
    }

    @PostMapping("/register")
    public String processRegister(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String gender,
            @RequestParam String nic,
            @RequestParam String address,
            @RequestParam String mobile,
            Model model) {

        if (!password.equals(confirmPassword)) {
            return "redirect:/register.html?error=nomatch";
        }

        try {
            User user = new User(firstName, lastName, email, gender, nic, address, mobile, password);
            User registered = userService.registerUser(user);

            if (registered != null) {
                // Do NOT auto-login after registration
                return "redirect:/login.html?registered=success";
            } else {
                return "redirect:/register.html?error=exists";
            }
        } catch (IOException e) {
            return "redirect:/register.html?error=system";
        }
    }
}
