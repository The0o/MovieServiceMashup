import exception.MovieNotFoundException;
import model.Movie;

import java.util.Date;
import java.util.List;

public interface MovieModel {

    public int addMovie(String title, int year, Date visualisationDate, int puntuation);

    public Movie findMovieByTitle(String title) throws MovieNotFoundException;

    public List<Movie> findMovieByYear(int year);

}
