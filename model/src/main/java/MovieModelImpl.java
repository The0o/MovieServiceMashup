import exception.MovieNotFoundException;
import model.Movie;

import java.util.Date;
import java.util.List;

public class MovieModelImpl implements MovieModel {

    @Override
    public int addMovie(String title, int year, Date visualisationDate, int puntuation) {
        return 0;
    }

    @Override
    public Movie findMovieByTitle(String title) throws MovieNotFoundException {
        return null;
    }

    @Override
    public List<Movie> findMovieByYear(int year) {
        return List.of();
    }
}
