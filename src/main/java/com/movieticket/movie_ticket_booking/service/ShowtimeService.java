package com.movieticket.movie_ticket_booking.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class ShowtimeService {
    private static final String SHOWTIME_FILE = "data/showtimes.txt";

    public ShowtimeService() {
        try {
            Files.createDirectories(Paths.get(SHOWTIME_FILE).getParent());
            if (!Files.exists(Paths.get(SHOWTIME_FILE))) {
                Files.createFile(Paths.get(SHOWTIME_FILE));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Assign a showtime to a movie-theater pair
    public void assignShowtime(String movieId, String theaterId, String showTime) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SHOWTIME_FILE), StandardOpenOption.APPEND)) {
            writer.write(movieId + "|" + theaterId + "|" + showTime);
            writer.newLine();
        }
    }

    // Get showtimes for a movie-theater pair (returns full showtime strings)
    public List<String> getShowtimes(String movieId, String theaterId) throws IOException {
        List<String> showtimes = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(SHOWTIME_FILE));
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length == 3 && parts[0].equals(movieId) && parts[1].equals(theaterId)) {
                showtimes.add(parts[2]);
            }
        }
        return showtimes;
    }

    // NEW: Get all unique dates for a movie-theater pair
    public List<String> getShowDates(String movieId, String theaterId) throws IOException {
        Set<String> dates = new LinkedHashSet<>();
        for (String showtime : getShowtimes(movieId, theaterId)) {
            // Accepts "YYYY-MM-DD HH:mm" or "YYYY-MM-DDTHH:mm"
            String date = showtime.split("[ T]")[0];
            dates.add(date);
        }
        return new ArrayList<>(dates);
    }

    // NEW: Get all times for a movie-theater pair on a specific date
    public List<String> getShowTimesForDate(String movieId, String theaterId, String date) throws IOException {
        List<String> times = new ArrayList<>();
        for (String showtime : getShowtimes(movieId, theaterId)) {
            String[] parts = showtime.split("[ T]");
            if (parts.length > 1 && parts[0].equals(date)) {
                times.add(parts[1]);
            }
        }
        return times;
    }
}
