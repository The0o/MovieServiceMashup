package model;

import exception.MovieNotFoundException;

import java.time.LocalDate;
import java.util.List;

/**
 *
 */
public interface MovieModel {

    void addMovie(String title, int year, LocalDate visualisationDate, int punctuation);

    Movie findMovieByTitle(String title) throws MovieNotFoundException;

    List<Movie> findMoviesByYear(int year);

}
