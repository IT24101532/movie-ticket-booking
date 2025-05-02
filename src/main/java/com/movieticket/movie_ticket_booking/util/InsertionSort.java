package com.movieticket.movie_ticket_booking.util;

import com.movieticket.movie_ticket_booking.model.Movie;

import java.util.List;

public class InsertionSort {

    /**
     * Sorts a list of movies by their release date using insertion sort.
     * @param movies List of Movie objects to be sorted.
     */
    public static void sortByReleaseDate(List<Movie> movies) {
        for (int i = 1; i < movies.size(); i++) {
            Movie key = movies.get(i);
            int j = i - 1;

            // Compare release dates and move elements accordingly
            while (j >= 0 && movies.get(j).getReleaseDate().isAfter(key.getReleaseDate())) {
                movies.set(j + 1, movies.get(j));
                j--;
            }
            movies.set(j + 1, key);
        }
    }
}
