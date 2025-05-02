package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.User;
import com.movieticket.movie_ticket_booking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

@Controller
public class UserController {
    private final UserService userService = new UserService();

    @GetMapping("/register")
    public String registerPage() {
        return "redirect:/register.html";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String email,
                               @RequestParam String gender,
                               @RequestParam String nic,
                               @RequestParam String address,
                               @RequestParam String mobile,
                               @RequestParam String password,
                               @RequestParam String confirmPassword) {
        if (!StringUtils.hasText(firstName) || !StringUtils.hasText(lastName) ||
                !StringUtils.hasText(email) || !StringUtils.hasText(gender) ||
                !StringUtils.hasText(nic) || !StringUtils.hasText(address) ||
                !StringUtils.hasText(mobile) || !StringUtils.hasText(password) ||
                !password.equals(confirmPassword)) {
            return "redirect:/register.html?error=invalid";
        }
        try {
            if (userService.findUserByEmail(email) != null) {
                return "redirect:/register.html?error=exists";
            }
            userService.saveUser(new User(firstName, lastName, email, gender, nic, address, mobile, password));
        } catch (Exception e) {
            return "redirect:/register.html?error=io";
        }
        return "redirect:/login.html?success=registered";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/login.html";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password) {
        try {
            User user = userService.findUserByEmailAndPassword(email, password);
            if (user != null) {
                return "redirect:/profile.html?firstName=" + user.getFirstName() + "&email=" + user.getEmail();
            } else {
                return "redirect:/login.html?error=invalid";
            }
        } catch (Exception e) {
            return "redirect:/login.html?error=io";
        }
    }
}
