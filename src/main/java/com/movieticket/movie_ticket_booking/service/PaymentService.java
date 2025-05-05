package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Payment;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PaymentService {
    private final String paymentFile = "src/main/resources/data/payments.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void savePayment(Payment payment) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(paymentFile, true));
        writer.write(payment.getPaymentId() + "," +
                payment.getBookingId() + "," +
                payment.getUserId() + "," +
                payment.getAmount() + "," +
                payment.getMethod() + "," +
                payment.getPaymentTime().format(FORMATTER) + "," +
                payment.getStatus());
        writer.newLine();
        writer.close();
    }

    public List<Payment> getAllPayments() throws IOException {
        List<Payment> payments = new ArrayList<>();
        File file = new File(paymentFile);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return payments;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 7) {
                payments.add(new Payment(
                        parts[0], parts[1], parts[2], Double.parseDouble(parts[3]),
                        parts[4], LocalDateTime.parse(parts[5], FORMATTER), parts[6]
                ));
            }
        }
        reader.close();
        return payments;
    }

    public List<Payment> getPaymentsByUser(String userId) throws IOException {
        List<Payment> all = getAllPayments();
        List<Payment> result = new ArrayList<>();
        for (Payment p : all) {
            if (p.getUserId().equals(userId)) result.add(p);
        }
        return result;
    }
}
