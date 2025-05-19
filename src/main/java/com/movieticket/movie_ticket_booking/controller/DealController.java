package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Deal;
import com.movieticket.movie_ticket_booking.service.DealService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/admin/deals")
public class DealController {
    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @GetMapping
    public String showDeals(Model model) {
        model.addAttribute("deals", dealService.getAllDeals());
        return "admin-deals";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("deal", new Deal());
        return "admin-deal-form";
    }

    @PostMapping("/save")
    public String saveDeal(@ModelAttribute Deal deal) throws IOException {
        dealService.saveDeal(deal);
        return "redirect:/admin/deals";
    }

    @GetMapping("/delete/{id}")
    public String deleteDeal(@PathVariable String id) throws IOException {
        dealService.deleteDeal(id);
        return "redirect:/admin/deals";
    }
}
