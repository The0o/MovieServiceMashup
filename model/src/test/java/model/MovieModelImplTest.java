package model;

import exception.MovieNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieModelImplTest {

    private final MovieModel model = MovieModelFactory.getModel();

    @Test
    void factoryReturnsSingleton() {
        assertSame(MovieModelFactory.getModel(), MovieModelFactory.getModel());
    }

    @Test
    void jsonIsLoadedWithVisualisationInfo() throws MovieNotFoundException {
        Movie movie = model.findMovieByTitle("The Godfather");
        assertEquals(1972, movie.getYear());
        assertNotNull(movie.getVisualisationInfo());
        assertEquals(LocalDate.of(2025, 1, 14), movie.getVisualisationInfo().getVisualisationDate());
        assertEquals(7, movie.getVisualisationInfo().getPunctuation()); // 6.7 arrondi
    }

    @Test
    void findMovieByTitleThrowsWhenMissing() {
        assertThrows(MovieNotFoundException.class, () -> model.findMovieByTitle("Film qui n'existe pas"));
    }

    @Test
    void addMovieAddsNewMovie() throws MovieNotFoundException {
        model.addMovie("Test Add", 2010, LocalDate.of(2025, 5, 1), 8);
        Movie movie = model.findMovieByTitle("Test Add");
        assertEquals(2010, movie.getYear());
        assertEquals(8, movie.getVisualisationInfo().getPunctuation());
    }

    @Test
    void addMovieUpdatesExistingMovie() throws MovieNotFoundException {
        model.addMovie("Test Update", 2000, LocalDate.of(2025, 1, 1), 3);
        model.addMovie("Test Update", 2001, LocalDate.of(2026, 2, 2), 9);

        Movie movie = model.findMovieByTitle("Test Update");
        assertEquals(2001, movie.getYear());
        assertEquals(LocalDate.of(2026, 2, 2), movie.getVisualisationInfo().getVisualisationDate());
        assertEquals(9, movie.getVisualisationInfo().getPunctuation());
        assertEquals(1, model.findMoviesByYear(2026).stream().filter(m -> m.getTitle().equals("Test Update")).count());
    }

    @Test
    void addMovieRejectsInvalidScore() {
        assertThrows(IllegalArgumentException.class, () -> model.addMovie("Bad", 2000, LocalDate.now(), 11));
    }

    @Test
    void findMoviesByYearUsesVisualisationDate() {
        List<Movie> movies = model.findMoviesByYear(2025);
        assertFalse(movies.isEmpty());
        assertTrue(movies.stream().allMatch(m -> m.getVisualisationInfo().getVisualisationDate().getYear() == 2025));
        assertTrue(model.findMoviesByYear(1972).isEmpty()); // annee de sortie du Parrain, pas de visualisation
    }
}
