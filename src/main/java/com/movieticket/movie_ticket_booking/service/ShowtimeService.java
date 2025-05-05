package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Showtime;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ShowtimeService {
    private static final String SHOWTIME_FILE = "data/showtimes.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ShowtimeService() {
        try {
            Files.createDirectories(Paths.get(SHOWTIME_FILE).getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(SHOWTIME_FILE))) {
                return showtimes;
            }

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(SHOWTIME_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        Showtime showtime = new Showtime();
                        showtime.setId(parts[0]);
                        showtime.setMovieId(parts[1]);
                        showtime.setTheaterId(parts[2]);
                        showtime.setShowtime(LocalDateTime.parse(parts[3], DATE_TIME_FORMATTER));
                        showtimes.add(showtime);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return showtimes;
    }

    // Add other methods for CRUD operations as needed
}
