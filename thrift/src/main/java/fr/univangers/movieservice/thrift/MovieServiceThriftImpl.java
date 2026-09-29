package fr.univangers.movieservice.thrift;

import org.apache.thrift.TException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exception.MovieNotFoundException;
import model.Movie;
import model.MovieModel;
import model.MovieModelFactory;

public class MovieServiceThriftImpl implements MovieService.Iface {

    private final MovieModel model = MovieModelFactory.getModel();

    private MovieDto toDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setTitle(movie.getTitle());
        dto.setYear((short) movie.getYear());

        if (movie.getVisualisationInfo() != null) {
            dto.setPoints((short) movie.getVisualisationInfo().getPunctuation());
            if (movie.getVisualisationInfo().getVisualisationDate() != null) {
                dto.setVisualisationDate(movie.getVisualisationInfo().getVisualisationDate().toString());
            }
        }
        return dto;
    }

    @Override
    public void addMovie(MovieDto movieDto) throws TException {
        LocalDate date = null;
        try {
            if (movieDto.getVisualisationDate() != null && !movieDto.getVisualisationDate().isBlank()) {
                date = LocalDate.parse(movieDto.getVisualisationDate());
            }
        } catch (DateTimeParseException e) {
            throw new TException("Veuillez respecter le format de date suviant : YYYY-MM-DD", e);
        }

        model.addMovie(movieDto.getTitle(), movieDto.getYear(), date, movieDto.getPoints());
    }

    @Override
    public MovieDto findMovieByTitle(String title) throws ServiceMovieNotFoundException, TException {
        try {
            Movie movie = model.findMovieByTitle(title);
            return toDto(movie);
        } catch (MovieNotFoundException e) {
            throw new ServiceMovieNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<MovieDto> findMoviesByYear(short year) throws TException {
        List<Movie> movies = model.findMoviesByYear(year);
        List<MovieDto> result = new ArrayList<>();

        for (Movie movie : movies) {
            result.add(toDto(movie));
        }
        return result;
    }
}



