package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Movie;
import com.movieticket.movie_ticket_booking.util.InsertionSort;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final String MOVIES_FILE = "src/main/resources/data/movies.txt";
    private List<Movie> movies;

    public MovieService() {
        this.movies = loadMovies();
        InsertionSort.sortByReleaseDate(movies);  // Sort right after loading
    }

    public List<Movie> getAllMovies() {
        InsertionSort.sortByReleaseDate(movies);  // Ensure sorted before returning
        return movies;
    }

    public Movie getMovieById(int id) {
        return movies.stream()
                .filter(movie -> movie.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Movie> searchMovies(String keyword) {
        String lowercaseKeyword = keyword.toLowerCase();
        return movies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(lowercaseKeyword) ||
                        movie.getGenre().toLowerCase().contains(lowercaseKeyword))
                .collect(Collectors.toList());
    }

    public List<Movie> filterByGenre(String genre) {
        return movies.stream()
                .filter(movie -> movie.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public void addMovie(Movie movie) {
        // Generate a new ID (simple implementation)
        int newId = movies.isEmpty() ? 1 :
                movies.stream().mapToInt(Movie::getId).max().getAsInt() + 1;
        movie.setId(newId);
        movies.add(movie);
        InsertionSort.sortByReleaseDate(movies);  // Sort after adding
        saveMovies();
    }

    public boolean updateMovie(int id, Movie updatedMovie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == id) {
                updatedMovie.setId(id);
                movies.set(i, updatedMovie);
                InsertionSort.sortByReleaseDate(movies);  // Sort after update
                saveMovies();
                return true;
            }
        }
        return false;
    }

    public boolean deleteMovie(int id) {
        boolean removed = movies.removeIf(movie -> movie.getId() == id);
        if (removed) {
            InsertionSort.sortByReleaseDate(movies);  // Sort after delete
            saveMovies();
        }
        return removed;
    }

    private List<Movie> loadMovies() {
        List<Movie> loadedMovies = new ArrayList<>();
        File file = new File(MOVIES_FILE);

        // Create file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return loadedMovies;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8) {
                    Movie movie = new Movie(
                            Integer.parseInt(data[0]),
                            data[1],
                            data[2],
                            LocalDate.parse(data[3]),
                            Integer.parseInt(data[4]),
                            data[5],
                            Double.parseDouble(data[6]),
                            data[7]
                    );
                    loadedMovies.add(movie);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedMovies;
    }

    private void saveMovies() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MOVIES_FILE))) {
            for (Movie movie : movies) {
                writer.write(movie.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
