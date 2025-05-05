package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Movie;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MovieService {
    private final String DATA_FILE = "data/movies.txt";
    private final String UPLOAD_DIR = "uploads/movies/";
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public MovieService() {
        // Create directories at startup
        try {
            Files.createDirectories(Paths.get("data"));
            Files.createDirectories(Paths.get("uploads/movies"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMovie(Movie movie, MultipartFile imageFile) throws IOException {
        // Generate unique ID
        movie.setId(String.valueOf(System.currentTimeMillis()));

        // Set defaults if not provided
        if (movie.getShowTime() == null) movie.setShowTime(LocalTime.of(18, 0));
        if (movie.getTheater() == null) movie.setTheater("Cinema Hall");
        if (movie.getEventType() == null) movie.setEventType("Movie");
        if (movie.getPrice() == null) movie.setPrice(300.0);

        // Save image and get path
        String imagePath = saveImage(imageFile);
        movie.setImagePath(imagePath);

        // Save movie data
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(DATA_FILE),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(String.join("|",
                    movie.getId(),
                    movie.getTitle(),
                    movie.getGenre(),
                    movie.getLanguage(),
                    movie.getReleaseDate().format(DATE_FORMATTER),
                    movie.getShowTime().format(TIME_FORMATTER),
                    movie.getTheater(),
                    movie.getDescription(),
                    movie.getImagePath(),
                    String.valueOf(movie.getPrice()),
                    movie.getEventType()
            ));
            writer.newLine();
        }
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return "/img/default-movie.jpg";
        }

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename().replace(" ", "_");
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/movies/" + fileName;
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        Path path = Paths.get(DATA_FILE);

        if (!Files.exists(path)) {
            return movies;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split("\\|", -1); // Keep empty strings

                    Movie movie = new Movie();
                    if (parts.length > 0) movie.setId(parts[0]);
                    if (parts.length > 1) movie.setTitle(parts[1]);
                    if (parts.length > 2) movie.setGenre(parts[2]);
                    if (parts.length > 3) movie.setLanguage(parts[3]);
                    if (parts.length > 4) movie.setReleaseDate(LocalDate.parse(parts[4], DATE_FORMATTER));
                    if (parts.length > 5) movie.setShowTime(LocalTime.parse(parts[5], TIME_FORMATTER));
                    if (parts.length > 6) movie.setTheater(parts[6]);
                    if (parts.length > 7) movie.setDescription(parts[7]);
                    if (parts.length > 8) movie.setImagePath(parts[8]);
                    if (parts.length > 9) movie.setPrice(Double.parseDouble(parts[9]));
                    if (parts.length > 10) movie.setEventType(parts[10]);

                    movies.add(movie);
                } catch (Exception e) {
                    System.err.println("Error parsing movie: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public Movie getMovieById(String id) {
        for (Movie movie : getAllMovies()) {
            if (movie.getId() != null && movie.getId().equals(id)) {
                return movie;
            }
        }
        return null;
    }
}
