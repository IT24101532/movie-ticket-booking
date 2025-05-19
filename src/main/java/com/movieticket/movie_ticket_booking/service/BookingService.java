package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Booking;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BookingService {
    private static final String BOOKING_FILE = "data/bookings.txt";
    private static final String DATA_DIR = "data";
    private static final DateTimeFormatter SHOWTIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Simple queue using a synchronized list
    private final List<Booking> bookingQueue = Collections.synchronizedList(new ArrayList<>());
    private final Object queueLock = new Object();

    public BookingService() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            createFileIfNotExists(BOOKING_FILE);
        } catch (IOException e) {
            System.err.println("Error initializing data files: " + e.getMessage());
        }

        // Start background thread to process the queue
        Thread processor = new Thread(() -> {
            while (true) {
                Booking nextBooking = null;
                synchronized (queueLock) {
                    if (!bookingQueue.isEmpty()) {
                        nextBooking = bookingQueue.remove(0);
                    }
                }
                if (nextBooking != null) {
                    try {
                        processBookingDirect(nextBooking);
                    } catch (IOException e) {
                        System.err.println("Failed to process booking: " + e.getMessage());
                    }
                } else {
                    try {
                        Thread.sleep(100); // Sleep briefly if queue is empty
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        processor.setDaemon(true);
        processor.start();
    }

    // Call this method from your controller to add a booking request to the queue
    public void queueBooking(Booking booking) {
        synchronized (queueLock) {
            bookingQueue.add(booking);
        }
        System.out.println("Booking added to queue: " + booking.getId());
    }

    // This method is used internally by the queue processor
    private Booking processBookingDirect(Booking booking) throws IOException {
        System.out.println("Processing booking: " + booking.getSeatNumbers() +
                " for movieId=" + booking.getMovieId() +
                ", theaterId=" + booking.getTheaterId() +
                ", showDateTime=" + booking.getShowDateTime());

        if (booking.getMovieId() == null || booking.getTheaterId() == null ||
                booking.getSeatNumbers() == null || booking.getShowDateTime() == null) {
            throw new IOException("Invalid booking data");
        }

        List<String> occupied = getOccupiedSeats(booking.getMovieId(), booking.getTheaterId(), booking.getShowDateTime());
        List<String> newSeats = Arrays.asList(booking.getSeatNumbers().split(","));

        for (String seat : newSeats) {
            if (occupied.contains(seat.trim())) {
                System.out.println("Seat already occupied: " + seat.trim());
                throw new IOException("Some seats are already occupied");
            }
        }

        saveBooking(booking);
        System.out.println("Booking saved: " + booking);
        return booking;
    }

    // (Keep all your other methods unchanged below)
    // ...

    public List<String> getOccupiedSeats(String movieId, String theaterId, String showtime) throws IOException {
        List<String> occupied = new ArrayList<>();
        List<Booking> allBookings = getAllBookings();
        String requestedShowtime = normalizeShowtime(showtime);

        for (Booking booking : allBookings) {
            String bookingShowtime = normalizeShowtime(booking.getShowDateTime());
            if (
                    safeEquals(booking.getMovieId(), movieId) &&
                            safeEquals(booking.getTheaterId(), theaterId) &&
                            safeEquals(bookingShowtime, requestedShowtime)
            ) {
                String[] seats = booking.getSeatNumbers().split(",");
                for (String seat : seats) {
                    if (!seat.trim().isEmpty()) occupied.add(seat.trim());
                }
            }
        }
        return occupied;
    }

    public List<String> getAllTimeOccupiedSeats(String movieId, String theaterId) throws IOException {
        Set<String> occupied = new HashSet<>();
        List<Booking> allBookings = getAllBookings();

        for (Booking booking : allBookings) {
            if (
                    safeEquals(booking.getMovieId(), movieId) &&
                            safeEquals(booking.getTheaterId(), theaterId)
            ) {
                String[] seats = booking.getSeatNumbers().split(",");
                for (String seat : seats) {
                    if (!seat.trim().isEmpty()) occupied.add(seat.trim());
                }
            }
        }
        return new ArrayList<>(occupied);
    }

    public Booking processBooking(Booking booking) throws IOException {
        // This method can be left as is for direct processing if needed,
        // but for queued processing, use queueBooking(booking)
        return processBookingDirect(booking);
    }

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
                normalizeShowtime(booking.getShowDateTime()),
                booking.getSeatNumbers(),
                String.valueOf(booking.getTotalAmount())
        );
    }

    public List<Booking> getBookingsByUser(String userId) throws IOException {
        return readBookings(parts -> parts.length >= 7 && safeEquals(parts[1], userId));
    }

    public List<Booking> getAllBookings() throws IOException {
        return readBookings(parts -> true);
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
        booking.setShowDateTime(parts[4]);
        booking.setSeatNumbers(parts[5]);
        booking.setTotalAmount(Double.parseDouble(parts[6]));
        return booking;
    }

    private void createFileIfNotExists(String path) throws IOException {
        if (!Files.exists(Paths.get(path))) {
            Files.createFile(Paths.get(path));
        }
    }

    public boolean cancelBooking(String bookingId, String userId) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(BOOKING_FILE));
        List<String> updated = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            String[] parts = line.split("\\|", 7);
            if (parts.length >= 7 && safeEquals(parts[0], bookingId) && safeEquals(parts[1], userId)) {
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

    private String normalizeShowtime(String showtime) {
        if (showtime == null || showtime.trim().isEmpty()) return "";
        try {
            String s = showtime.trim().replace("T", " ");
            if (s.length() > 16) s = s.substring(0, 16);
            LocalDateTime dt = LocalDateTime.parse(s, SHOWTIME_FORMAT);
            return dt.format(SHOWTIME_FORMAT);
        } catch (Exception e) {
            return showtime.trim().replace("T", " ").substring(0, Math.min(16, showtime.length()));
        }
    }

    private boolean safeEquals(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}