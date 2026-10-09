package fr.univangers.movieservice.thrift;

import org.apache.thrift.TException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import exception.MovieNotFoundException;
import model.Movie;
import model.MovieModel;
import model.MovieModelFactory;

/**
 * Implémentation du service Thrift pour la gestion des films.
 * Fait le lien entre l'interface réseau générée par Thrift et le modèle métier interne.
 */
public class MovieServiceThriftImpl implements MovieService.Iface {

    private final MovieModel model = MovieModelFactory.getModel();

    /**
     * Convertit un objet métier Movie en DTO MovieDto.
     *
     * @param movie le film issu du modèle
     * @return le film converti au format attendu par l'interface Thrift
     */
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

    /**
     * Ajoute un film à partir du DTO reçu, ou met à jour celui qui porte déjà ce titre.
     *
     * @param movieDto les informations du film à ajouter ou à mettre à jour
     * @throws TException si la date de visualisation fournie ne respecte pas le format attendu (AAAA-MM-JJ)
     */
    @Override
    public void addMovie(MovieDto movieDto) throws TException {
        LocalDate date = null;
        try {
            if (movieDto.getVisualisationDate() != null && !movieDto.getVisualisationDate().isBlank()) {
                date = LocalDate.parse(movieDto.getVisualisationDate());
            }
        } catch (DateTimeParseException e) {
            throw new TException("Veuillez respecter le format de date suivant : AAAA-MM-JJ", e);
        }

        model.addMovie(movieDto.getTitle(), movieDto.getYear(), date, movieDto.getPoints());
    }

    /**
     * Recherche un film à partir de son titre
     *
     * @param title le titre du film recherché
     * @return le film trouvé sous forme de DTO
     * @throws ServiceMovieNotFoundException si aucun film n'a été trouvé
     * @throws TException pour toute autre erreur système liée au protocole Thrift
     */
    @Override
    public MovieDto findMovieByTitle(String title) throws ServiceMovieNotFoundException, TException {
        try {
            Movie movie = model.findMovieByTitle(title);
            return toDto(movie);
        } catch (MovieNotFoundException e) {
            throw new ServiceMovieNotFoundException(e.getMessage());
        }
    }

    /**
     * Récupère l'ensemble des films vus au cours de l'année précisée
     *
     * @param year l'année de visionnage des films à rechercher
     * @return une liste contenant les films trouvés (vide s'il n'y en a aucun)
     * @throws TException pour toute erreur système liée au protocole Thrift
     */
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