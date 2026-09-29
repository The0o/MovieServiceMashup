package tmdb.client;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tmdb.dto.CharacterDto;
import tmdb.dto.MovieInfoDto;
import tmdb.exception.MovieInfoNotFoundException;
import tmdb.exception.MovieInfoServiceException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TMDbClientImplTest {

    // Reponses reprises de l'enonce (The Matrix)
    private static final Map<String, String> RESPONSES = Map.of(
            "/3/search/movie", """
                    {"results": [{"id": 999, "title": "The Matrix Reloaded"}, {"id": 603, "title": "The Matrix"}]}""",
            "/3/movie/603", """
                    {"id": 603, "title": "The Matrix", "release_date": "1999-03-30",
                     "poster_path": "/aOIuZAjPaRIE6CMzbazvcHuHXDc.jpg",
                     "genres": [{"id": 28, "name": "Action"}, {"id": 878, "name": "Science Fiction"}]}""",
            "/3/movie/603/credits", """
                    {"id": 603, "cast": [
                      {"id": 6384, "name": "Keanu Reeves", "character": "Neo", "profile_path": "/8RZLOyYGsoRe9p44q3xin9QkMHv.jpg"},
                      {"id": 2975, "name": "Laurence Fishburne", "character": "Morpheus", "profile_path": "/2GbXERENPpl5MmlqOLlPVaVtifD.jpg"}]}""",
            "/3/person/6384", """
                    {"birthday": "1964-09-02", "deathday": null, "id": 6384, "name": "Keanu Reeves",
                     "place_of_birth": "Beirut, Lebanon", "profile_path": "/8RZLOyYGsoRe9p44q3xin9QkMHv.jpg"}""");

    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void startFakeTmdb() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String body;
            int status;
            if (query == null || !query.contains("api_key=test-key")) {
                status = 401;
                body = "{}";
            } else if (query.contains("query=Inconnu")) {
                status = 200;
                body = "{\"results\": []}";
            } else {
                body = RESPONSES.get(exchange.getRequestURI().getPath());
                status = body == null ? 404 : 200;
                body = body == null ? "{}" : body;
            }
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });
        server.start();
        baseUrl = "http://localhost:" + server.getAddress().getPort() + "/3";
    }

    @AfterEach
    void stopFakeTmdb() {
        server.stop(0);
    }

    @Test
    void findMovieInformationCombinesTheThreeQueries() throws MovieInfoNotFoundException {
        MovieInfoDto movie = new TMDbClientImpl("test-key", baseUrl).findMovieInformation("the matrix");

        assertEquals("The Matrix", movie.getTitle());
        assertEquals(1999, movie.getYear());
        assertEquals(java.util.List.of("Action", "Science Fiction"), movie.getGenres());
        assertEquals("https://image.tmdb.org/t/p/w500/aOIuZAjPaRIE6CMzbazvcHuHXDc.jpg", movie.getPosterPath().toString());
        assertEquals(2, movie.getCharacters().size());

        CharacterDto neo = movie.getCharacters().get(0);
        assertEquals("Neo", neo.getCharacterName());
        assertEquals("Keanu Reeves", neo.getActorName());
        assertEquals("1964-09-02", neo.getBirthday());
        assertNull(neo.getDeathday());
        assertEquals("Beirut, Lebanon", neo.getPlaceOfBirth());
        assertEquals("https://image.tmdb.org/t/p/w500/8RZLOyYGsoRe9p44q3xin9QkMHv.jpg", neo.getImgUrl().toString());
    }

    @Test
    void characterIsKeptWhenPersonRequestFails() throws MovieInfoNotFoundException {
        CharacterDto morpheus = new TMDbClientImpl("test-key", baseUrl).findMovieInformation("The Matrix").getCharacters().get(1);

        assertEquals("Morpheus", morpheus.getCharacterName());
        assertEquals("Laurence Fishburne", morpheus.getActorName());
        assertNull(morpheus.getBirthday());
        assertNotNull(morpheus.getImgUrl());
    }

    @Test
    void unknownTitleThrowsMovieInfoNotFoundException() {
        assertThrows(MovieInfoNotFoundException.class,
                () -> new TMDbClientImpl("test-key", baseUrl).findMovieInformation("Inconnu"));
    }

    @Test
    void invalidApiKeyThrowsMovieInfoServiceException() {
        assertThrows(MovieInfoServiceException.class,
                () -> new TMDbClientImpl("mauvaise-cle", baseUrl).findMovieInformation("The Matrix"));
    }

    @Test
    void factoryReturnsSingleton() {
        System.setProperty("tmdb.api.key", "test-key");
        assertSame(MovieInformationClientFactory.getClient(), MovieInformationClientFactory.getClient());
    }
}
