import exception.MovieNotFoundException;
import model.Movie;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieModelImpl implements MovieModel {

    private List<Movie> movies;

    @Override
    public int addMovie(String title, int year, Date visualisationDate, int puntuation) {
        this.movies.add(new Movie(title, year, visualisationDate, puntuation));
        return 0;
    }

    @Override
    public Movie findMovieByTitle(String title) throws MovieNotFoundException {
        Optional<Movie> movie = movies.stream().filter(m -> m.getTitle().equalsIgnoreCase(title)).findFirst();
        //en soit le findFirst() sert a rien vu que le titre est cense etre l'identifiant unique mais je le garde en garde-fou
        if (movie.isEmpty()) {
            throw new MovieNotFoundException("Pas de film trouvé");
        }
        return movie.get();
    }

    @Override
    public List<Movie> findMovieByYear(int year) {
        List<Movie> listResult = movies.stream().filter(m -> m.getYear() == year).toList();
        return listResult;
    }
}
