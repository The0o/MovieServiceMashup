package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.MovieNotFoundException;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieModelImpl implements MovieModel {

    private static final String DATA_FILE = "movies_100.json";

    private static final List<Movie> movies = new ArrayList<>();

    // Chargement des films au chargement de la classe
    static {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try (InputStream inputStream = MovieModelImpl.class.getClassLoader().getResourceAsStream(DATA_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException("Fichier " + DATA_FILE + " introuvable");
            }
            movies.addAll(mapper.readValue(inputStream, new TypeReference<List<Movie>>() {}));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // Package-private : seule MovieModelFactory doit creer l'instance
    MovieModelImpl() {
    }

    @Override
    public synchronized void addMovie(String title, int year, LocalDate visualisationDate, int punctuation) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }
        if (punctuation < 0 || punctuation > 10) {
            throw new IllegalArgumentException("La note doit etre comprise entre 0 et 10");
        }
        if (year < 0) {
            throw new IllegalArgumentException("L'annee doit etre positive");
        }

        Optional<Movie> existing = findByTitle(title);
        if (existing.isPresent()) {
            // Le titre sert d'identifiant : on met a jour le film existant
            Movie movie = existing.get();
            movie.setYear(year);
            movie.setVisualisationInfo(new VisualisationInfo(visualisationDate, punctuation));
        } else {
            movies.add(new Movie(title, year, visualisationDate, punctuation));
        }
    }

    @Override
    public synchronized Movie findMovieByTitle(String title) throws MovieNotFoundException {
        return findByTitle(title)
                .orElseThrow(() -> new MovieNotFoundException("Pas de film trouve avec le titre : " + title));
    }

    @Override
    public synchronized List<Movie> findMoviesByYear(int year) {
        // Films vus pendant l'annee donnee (date de visualisation, pas date de sortie)
        return movies.stream()
                .filter(m -> m.getVisualisationInfo() != null
                        && m.getVisualisationInfo().getVisualisationDate() != null
                        && m.getVisualisationInfo().getVisualisationDate().getYear() == year)
                .toList();
    }

    private Optional<Movie> findByTitle(String title) {
        if (title == null) {
            return Optional.empty();
        }
        return movies.stream().filter(m -> m.getTitle().equalsIgnoreCase(title)).findFirst();
    }
}
