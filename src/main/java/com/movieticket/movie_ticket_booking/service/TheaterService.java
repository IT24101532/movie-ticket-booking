package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Theater;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TheaterService {
    private static final String THEATER_FILE = "data/theaters.txt";

    public TheaterService() {
        try {
            Files.createDirectories(Paths.get(THEATER_FILE).getParent());
            if (!Files.exists(Paths.get(THEATER_FILE))) {
                Files.createFile(Paths.get(THEATER_FILE));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Theater getTheaterById(String theaterId) throws IOException {
        return getAllTheaters().stream()
                .filter(t -> t.getId().equals(theaterId))
                .findFirst()
                .orElse(null);
    }

    public List<Theater> getAllTheaters() throws IOException {
        List<Theater> theaters = new ArrayList<>();
        if (!Files.exists(Paths.get(THEATER_FILE))) return theaters;

        List<String> lines = Files.readAllLines(Paths.get(THEATER_FILE));
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                Theater theater = new Theater();
                theater.setId(parts[0]);
                theater.setName(parts[1]);
                theater.setLocation(parts[2]);
                theater.setTotalSeats(Integer.parseInt(parts[3]));
                theater.setSeatRows(Integer.parseInt(parts[4]));
                // FIX: Always use a mutable ArrayList for movieIds
                if (parts.length > 5 && !parts[5].isEmpty()) {
                    theater.setMovieIds(new ArrayList<>(Arrays.asList(parts[5].split(","))));
                } else {
                    theater.setMovieIds(new ArrayList<>());
                }
                theaters.add(theater);
            }
        }
        return theaters;
    }

    public void addTheater(Theater theater) throws IOException {
        theater.setId(UUID.randomUUID().toString());
        List<Theater> theaters = getAllTheaters();
        theaters.add(theater);
        saveAllTheaters(theaters);
    }

    public void deleteTheater(String theaterId) throws IOException {
        List<Theater> theaters = getAllTheaters().stream()
                .filter(t -> !t.getId().equals(theaterId))
                .collect(Collectors.toList());
        saveAllTheaters(theaters);
    }

    public void addMovieToTheater(String theaterId, String movieId) throws IOException {
        List<Theater> theaters = getAllTheaters();
        for (Theater t : theaters) {
            if (t.getId().equals(theaterId)) {
                // Defensive: ensure movieIds is always mutable
                if (t.getMovieIds() == null) {
                    t.setMovieIds(new ArrayList<>());
                } else if (!(t.getMovieIds() instanceof ArrayList)) {
                    t.setMovieIds(new ArrayList<>(t.getMovieIds()));
                }
                if (!t.getMovieIds().contains(movieId)) {
                    t.getMovieIds().add(movieId);
                }
                break;
            }
        }
        saveAllTheaters(theaters);
    }

    public List<Theater> getTheatersByMovieId(String movieId) throws IOException {
        return getAllTheaters().stream()
                .filter(t -> t.getMovieIds().contains(movieId))
                .collect(Collectors.toList());
    }

    private void saveAllTheaters(List<Theater> theaters) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(THEATER_FILE))) {
            for (Theater t : theaters) {
                String movieIds = String.join(",", t.getMovieIds());
                writer.write(String.join("|",
                        t.getId(),
                        t.getName(),
                        t.getLocation(),
                        String.valueOf(t.getTotalSeats()),
                        String.valueOf(t.getSeatRows()),
                        movieIds
                ));
                writer.newLine();
            }
        }
    }
}
