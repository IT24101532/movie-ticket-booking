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
    private static final String SEATS_FILE = "data/seats.txt";
    private static final String DATA_DIR = "data";

    public BookingService() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            createFileIfNotExists(BOOKING_FILE);
            createFileIfNotExists(SEATS_FILE);
        } catch (IOException e) {
            System.err.println("Error initializing data files: " + e.getMessage());
        }
    }

    // Main booking processing with enhanced validation
    public Booking processBooking(Booking booking) throws IOException {
        if (booking.getMovieId() == null || booking.getTheaterId() == null || booking.getSeatNumbers() == null) {
            throw new IOException("Invalid booking data");
        }

        List<String> occupied = getOccupiedSeats(booking.getMovieId(), booking.getTheaterId());
        List<String> newSeats = Arrays.asList(booking.getSeatNumbers().split(","));

        if (Collections.disjoint(occupied, newSeats)) {
            saveBooking(booking);
            markSeatsAsOccupied(booking);
            return booking;
        }
        throw new IOException("Some seats are already occupied");
    }

    // Seat management with strict validation
    public void markSeatsAsOccupied(Booking booking) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(SEATS_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            for (String seat : booking.getSeatNumbers().split(",")) {
                String trimmedSeat = seat.trim();
                if (isValidSeatEntry(booking.getMovieId(), booking.getTheaterId(), trimmedSeat)) {
                    writer.write(String.join("|",
                            booking.getMovieId(),
                            booking.getTheaterId(),
                            trimmedSeat,
                            "OCCUPIED"));
                    writer.newLine();
                }
            }
        }
    }

    private boolean isValidSeatEntry(String movieId, String theaterId, String seat) {
        return !movieId.isEmpty() &&
                !theaterId.isEmpty() &&
                !seat.isEmpty() &&
                seat.matches("[A-Za-z]\\d+");
    }

    // Enhanced seat status retrieval
    public List<String> getOccupiedSeats(String movieId, String theaterId) throws IOException {
        List<String> occupied = new ArrayList<>();
        if (!Files.exists(Paths.get(SEATS_FILE))) return occupied;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(SEATS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split("\\|", 4);
                    if (parts.length == 4 &&
                            parts[0].equals(movieId) &&
                            parts[1].equals(theaterId) &&
                            parts[3].equals("OCCUPIED")) {
                        occupied.add(parts[2].trim());
                    }
                } catch (Exception e) {
                    System.err.println("Invalid seat entry: " + line);
                }
            }
        }
        return occupied;
    }

    // Core booking methods
    private void saveBooking(Booking booking) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(BOOKING_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(formatBooking(booking));
            writer.newLine();
        }
    }

    private String formatBooking(Booking booking) {
        return String.join("|",
                booking.getId() != null ? booking.getId() : UUID.randomUUID().toString(),
                booking.getUserId(),
                booking.getMovieId(),
                booking.getTheaterId(),
                LocalDateTime.now().toString(),
                booking.getSeatNumbers(),
                String.valueOf(booking.getTotalAmount()));
    }

    // Existing methods with improved error handling
    public List<Booking> getBookingsByUser(String userId) throws IOException {
        return readBookings(parts -> parts.length >= 7 && parts[1].equals(userId));
    }

    public boolean cancelBooking(String bookingId, String userId) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(BOOKING_FILE));
        List<String> updated = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            String[] parts = line.split("\\|", 7);
            if (parts.length >= 7 && parts[0].equals(bookingId) && parts[1].equals(userId)) {
                freeSeats(parts[2], parts[3], parts[5]);
                found = true;
            } else {
                updated.add(line);
            }
        }

        if (found) {
            Files.write(Paths.get(BOOKING_FILE), updated);
        }
        return found;
    }

    private void freeSeats(String movieId, String theaterId, String seats) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(SEATS_FILE));
        List<String> updated = new ArrayList<>();
        Set<String> targetSeats = new HashSet<>(Arrays.asList(seats.split(",")));

        for (String line : lines) {
            String[] parts = line.split("\\|", 4);
            if (parts.length != 4 ||
                    !parts[0].equals(movieId) ||
                    !parts[1].equals(theaterId) ||
                    !targetSeats.contains(parts[2].trim())) {
                updated.add(line);
            }
        }

        Files.write(Paths.get(SEATS_FILE), updated);
    }

    // Helper methods
    private void createFileIfNotExists(String path) throws IOException {
        if (!Files.exists(Paths.get(path))) {
            Files.createFile(Paths.get(path));
        }
    }

    private List<Booking> readBookings(java.util.function.Predicate<String[]> filter) throws IOException {
        List<Booking> bookings = new ArrayList<>();
        if (!Files.exists(Paths.get(BOOKING_FILE))) return bookings;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(BOOKING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 7);
                if (parts.length >= 7 && filter.test(parts)) {
                    bookings.add(createBookingFromParts(parts));
                }
            }
        }
        return bookings;
    }

    private Booking createBookingFromParts(String[] parts) {
        Booking booking = new Booking();
        booking.setId(parts[0]);
        booking.setUserId(parts[1]);
        booking.setMovieId(parts[2]);
        booking.setTheaterId(parts[3]);
        booking.setBookingTime(LocalDateTime.parse(parts[4]));
        booking.setSeatNumbers(parts[5]);
        booking.setTotalAmount(Double.parseDouble(parts[6]));
        return booking;
    }

    public List<Booking> getAllBookings() throws IOException {
        return readBookings(parts -> true);
    }
}
