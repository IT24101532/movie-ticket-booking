package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Booking;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingService {
    private static final String BOOKING_FILE = "data/bookings.txt";
    private static final String BOOKING_DIR = "data";

    public BookingService() {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get(BOOKING_DIR));
            if (!Files.exists(Paths.get(BOOKING_FILE))) {
                Files.createFile(Paths.get(BOOKING_FILE));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Process a new booking
    public Booking processBooking(Booking booking) throws IOException {
        // Generate a unique ID if none exists
        if (booking.getId() == null || booking.getId().isEmpty()) {
            booking.setId(String.valueOf(System.currentTimeMillis()));
        }

        // Set booking time to now if not set
        if (booking.getBookingTime() == null) {
            booking.setBookingTime(LocalDateTime.now());
        }

        // Save booking to file
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(BOOKING_FILE),
                StandardOpenOption.APPEND)) {
            writer.write(String.join("|",
                    booking.getId(),
                    booking.getUserId(),
                    booking.getShowtimeId(),
                    booking.getBookingTime().toString(),
                    booking.getSeatNumbers(),
                    String.valueOf(booking.getTotalAmount())
            ));
            writer.newLine();
        }

        return booking;
    }

    // Get bookings by user ID
    public List<Booking> getBookingsByUser(String userId) throws IOException {
        List<Booking> userBookings = new ArrayList<>();

        if (!Files.exists(Paths.get(BOOKING_FILE))) {
            return userBookings;
        }

        List<String> lines = Files.readAllLines(Paths.get(BOOKING_FILE));
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6 && parts[1].equals(userId)) {
                userBookings.add(createBookingFromParts(parts));
            }
        }

        return userBookings;
    }

    // Get booking by ID
    public Booking getBookingById(String bookingId) throws IOException {
        if (!Files.exists(Paths.get(BOOKING_FILE))) {
            return null;
        }

        List<String> lines = Files.readAllLines(Paths.get(BOOKING_FILE));
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6 && parts[0].equals(bookingId)) {
                return createBookingFromParts(parts);
            }
        }

        return null;
    }

    // Cancel a booking
    public boolean cancelBooking(String bookingId, String userId) throws IOException {
        if (!Files.exists(Paths.get(BOOKING_FILE))) {
            return false;
        }

        List<String> lines = Files.readAllLines(Paths.get(BOOKING_FILE));
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6 && parts[0].equals(bookingId) && parts[1].equals(userId)) {
                found = true;
                // Skip this line (removing it from the file)
            } else {
                updatedLines.add(line);
            }
        }

        if (found) {
            Files.write(Paths.get(BOOKING_FILE), updatedLines);
        }

        return found;
    }

    // Get all bookings (for admin dashboard, etc.)
    public List<Booking> getAllBookings() {
        List<Booking> allBookings = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(BOOKING_FILE))) {
                return allBookings;
            }

            List<String> lines = Files.readAllLines(Paths.get(BOOKING_FILE));
            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    allBookings.add(createBookingFromParts(parts));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allBookings;
    }

    // Helper method to create a Booking object from parts
    private Booking createBookingFromParts(String[] parts) {
        Booking booking = new Booking();
        booking.setId(parts[0]);
        booking.setUserId(parts[1]);
        booking.setShowtimeId(parts[2]);
        booking.setBookingTime(LocalDateTime.parse(parts[3]));
        booking.setSeatNumbers(parts[4]);
        booking.setTotalAmount(Double.parseDouble(parts[5]));
        return booking;
    }
}
