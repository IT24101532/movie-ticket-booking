package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Movie;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    private final String movieFile = "src/main/resources/data/movies.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void saveMovie(Movie movie) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(movieFile, true));
        writer.write(movie.getTitle() + "," +
                movie.getGenre() + "," +
                movie.getLanguage() + "," +
                movie.getReleaseDate().format(DATE_FORMATTER) + "," +
                movie.getDescription());
        writer.newLine();
        writer.close();
    }

    public List<Movie> getAllMovies() throws IOException {
        List<Movie> movies = new ArrayList<>();
        File file = new File(movieFile);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return movies;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                Movie movie = new Movie(
                        parts[0], // title
                        parts[1], // genre
                        parts[2], // language
                        LocalDate.parse(parts[3], DATE_FORMATTER), // releaseDate
                        parts[4]  // description
                );
                movies.add(movie);
            }
        }
        reader.close();
        return movies;
    }

    // Insertion Sort by release date
    public List<Movie> sortMoviesByReleaseDate(List<Movie> movies) {
        for (int i = 1; i < movies.size(); i++) {
            Movie key = movies.get(i);
            int j = i - 1;
            while (j >= 0 && movies.get(j).getReleaseDate().isAfter(key.getReleaseDate())) {
                movies.set(j + 1, movies.get(j));
                j--;
            }
            movies.set(j + 1, key);
        }
        return movies;
    }
}
