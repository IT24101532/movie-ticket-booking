package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Theater;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class TheaterService {
    private static final String THEATER_FILE = "data/theaters.txt";

    public TheaterService() {
        try {
            Files.createDirectories(Paths.get(THEATER_FILE).getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Theater> getAllTheaters() {
        List<Theater> theaters = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(THEATER_FILE))) {
                return theaters;
            }

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(THEATER_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        Theater theater = new Theater();
                        theater.setId(parts[0]);
                        theater.setName(parts[1]);
                        theater.setLocation(parts[2]);
                        theaters.add(theater);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return theaters;
    }

    // Add other methods for CRUD operations as needed
}
