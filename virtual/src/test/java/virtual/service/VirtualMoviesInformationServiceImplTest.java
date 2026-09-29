package virtual.service;

import org.junit.jupiter.api.Test;
import tmdb.client.MovieInformationClient;
import tmdb.dto.MovieInfoDto;
import tmdb.exception.MovieInfoNotFoundException;
import virtual.client.MovieViewingClient;
import virtual.dto.MovieViewingDto;
import virtual.dto.VirtualServiceMovieDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VirtualMoviesInformationServiceImplTest {

    private final MovieInformationClient tmdb = title -> {
        if (title.equalsIgnoreCase("amelie") || title.equalsIgnoreCase("The Matrix")) {
            String officialTitle = title.equalsIgnoreCase("amelie") ? "Amélie" : "The Matrix";
            return new MovieInfoDto(officialTitle, List.of("Action"), null, 1999, List.of());
        }
        throw new MovieInfoNotFoundException("introuvable");
    };

    private final List<String> thriftCalls = new ArrayList<>();

    private final MovieViewingClient thrift = title -> {
        thriftCalls.add(title);
        return title.equals("Amélie") || title.equals("The Matrix")
                ? Optional.of(new MovieViewingDto("2025-03-01", 8))
                : Optional.empty();
    };

    private final VirtualMoviesInformationService service = new VirtualMoviesInformationServiceImpl(tmdb, thrift);

    @Test
    void combinesTmdbAndThriftInformation() throws MovieInfoNotFoundException {
        VirtualServiceMovieDTO movie = service.findMovieInformation("The Matrix");

        assertEquals("The Matrix", movie.getTitle());
        assertEquals(1999, movie.getYear());
        assertEquals("2025-03-01", movie.getVisualisationDate());
        assertEquals(8, movie.getPoints());
    }

    @Test
    void retriesThriftWithOfficialTmdbTitle() throws MovieInfoNotFoundException {
        VirtualServiceMovieDTO movie = service.findMovieInformation("amelie");

        assertEquals(List.of("amelie", "Amélie"), thriftCalls);
        assertEquals(8, movie.getPoints());
    }

    @Test
    void movieNotSeenByWalterHasNoViewingInformation() throws MovieInfoNotFoundException {
        MovieViewingClient emptyThrift = title -> Optional.empty();
        VirtualServiceMovieDTO movie = new VirtualMoviesInformationServiceImpl(tmdb, emptyThrift).findMovieInformation("The Matrix");

        assertEquals("The Matrix", movie.getTitle());
        assertNull(movie.getVisualisationDate());
        assertNull(movie.getPoints());
    }

    @Test
    void unknownMovieThrowsNotFound() {
        assertThrows(MovieInfoNotFoundException.class, () -> service.findMovieInformation("Inconnu"));
    }

    @Test
    void blankTitleIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.findMovieInformation("  "));
    }
}
