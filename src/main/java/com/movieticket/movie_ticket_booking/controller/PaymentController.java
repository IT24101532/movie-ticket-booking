package com.movieticket.movie_ticket_booking.controller;

import com.movieticket.movie_ticket_booking.model.Payment;
import com.movieticket.movie_ticket_booking.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class PaymentController {
    private final PaymentService paymentService = new PaymentService();

    @GetMapping("/payment")
    public String showPaymentPage() {
        return "payment.html";
    }

    @GetMapping("/payment-history")
    public String showPaymentHistoryPage() {
        return "payment-history.html";
    }

    @GetMapping("/refund")
    public String showRefundPage() {
        return "refund.html";
    }

    @PostMapping("/api/payments")
    @ResponseBody
    public Payment makePayment(@RequestBody Payment payment) throws IOException {
        paymentService.savePayment(payment);
        return payment;
    }

    @GetMapping("/api/payments")
    @ResponseBody
    public List<Payment> getUserPayments(@RequestParam String userId) throws IOException {
        return paymentService.getPaymentsByUser(userId);
    }
}
