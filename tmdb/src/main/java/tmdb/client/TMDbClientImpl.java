package tmdb.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tmdb.dto.CharacterDto;
import tmdb.dto.MovieInfoDto;
import tmdb.exception.MovieInfoNotFoundException;
import tmdb.exception.MovieInfoServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TMDbClientImpl implements MovieInformationClient {

    private static final String DEFAULT_BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    TMDbClientImpl(String apiKey) {
        this(apiKey, DEFAULT_BASE_URL);
    }

    // Package-private : permet aux tests de viser un faux serveur TMDb
    TMDbClientImpl(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    @Override
    public MovieInfoDto findMovieInformation(String title) throws MovieInfoNotFoundException {
        if (title == null || title.isBlank()) {
            throw new MovieInfoNotFoundException("Le titre est obligatoire");
        }

        // 1. Recherche du film par titre pour obtenir son id
        long movieId = searchMovieId(title);

        // 2. Details et credits du film, recuperes en parallele
        CompletableFuture<JsonNode> detailsFuture = getAsync("/movie/" + movieId);
        CompletableFuture<JsonNode> creditsFuture = getAsync("/movie/" + movieId + "/credits");
        JsonNode details = join(detailsFuture);
        JsonNode credits = join(creditsFuture);
        if (details == null) {
            throw new MovieInfoNotFoundException("Pas d'information trouvee pour le film : " + title);
        }

        // 3. Informations biographiques de chaque acteur
        List<CharacterDto> characters = credits == null ? new ArrayList<>() : findCharacters(credits.path("cast"));

        return new MovieInfoDto(
                details.path("title").asText(title),
                findGenres(details.path("genres")),
                toImageUrl(details.path("poster_path")),
                toYear(details.path("release_date")),
                characters);
    }

    private long searchMovieId(String title) throws MovieInfoNotFoundException {
        JsonNode search = join(getAsync("/search/movie?query=" + URLEncoder.encode(title, StandardCharsets.UTF_8)));
        JsonNode results = search == null ? null : search.path("results");
        if (results == null || results.isEmpty()) {
            throw new MovieInfoNotFoundException("Pas de film trouve sur TMDb avec le titre : " + title);
        }
        // On privilegie un titre identique, sinon le premier resultat (le plus pertinent pour TMDb)
        for (JsonNode result : results) {
            if (result.path("title").asText().equalsIgnoreCase(title)
                    || result.path("original_title").asText().equalsIgnoreCase(title)) {
                return result.path("id").asLong();
            }
        }
        return results.get(0).path("id").asLong();
    }

    private List<String> findGenres(JsonNode genresNode) {
        List<String> genres = new ArrayList<>();
        for (JsonNode genre : genresNode) {
            genres.add(genre.path("name").asText());
        }
        return genres;
    }

    private List<CharacterDto> findCharacters(JsonNode castNode) {
        List<JsonNode> cast = new ArrayList<>();
        for (JsonNode actor : castNode) {
            cast.add(actor);
        }

        // Une requete /person par acteur, lancees en parallele
        List<CompletableFuture<JsonNode>> persons = new ArrayList<>();
        for (JsonNode actor : cast) {
            persons.add(getAsync("/person/" + actor.path("id").asLong()).exceptionally(e -> null));
        }

        List<CharacterDto> characters = new ArrayList<>();
        for (int i = 0; i < cast.size(); i++) {
            JsonNode actor = cast.get(i);
            // Si la requete /person echoue, on garde quand meme le personnage avec les infos des credits
            JsonNode person = persons.get(i).join();
            JsonNode profilePath = person != null && !isEmpty(person.path("profile_path"))
                    ? person.path("profile_path") : actor.path("profile_path");
            characters.add(new CharacterDto(
                    textOrNull(actor.path("character")),
                    toImageUrl(profilePath),
                    textOrNull(actor.path("name")),
                    person == null ? null : textOrNull(person.path("birthday")),
                    person == null ? null : textOrNull(person.path("deathday")),
                    person == null ? null : textOrNull(person.path("place_of_birth"))));
        }
        return characters;
    }

    // Renvoie null si la ressource n'existe pas (404), leve MovieInfoServiceException pour les autres erreurs
    private CompletableFuture<JsonNode> getAsync(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET();
        String url = baseUrl + path;
        if (isBearerToken()) {
            builder.header("Authorization", "Bearer " + apiKey);
        } else {
            url += (path.contains("?") ? "&" : "?") + "api_key=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
        }

        return httpClient.sendAsync(builder.uri(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    if (status == 404) {
                        return null;
                    }
                    if (status == 401) {
                        throw new MovieInfoServiceException("Cle API TMDb invalide");
                    }
                    if (status != 200) {
                        throw new MovieInfoServiceException("Erreur TMDb (HTTP " + status + ") sur " + path);
                    }
                    try {
                        return mapper.readTree(response.body());
                    } catch (IOException e) {
                        throw new MovieInfoServiceException("Reponse TMDb illisible sur " + path, e);
                    }
                });
    }

    private JsonNode join(CompletableFuture<JsonNode> future) {
        try {
            return future.join();
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            if (cause instanceof MovieInfoServiceException serviceException) {
                throw serviceException;
            }
            throw new MovieInfoServiceException("Impossible de contacter TMDb", cause);
        }
    }

    // Une cle v4 (jeton de lecture) est un JWT, a envoyer dans l'en-tete Authorization
    private boolean isBearerToken() {
        return apiKey.startsWith("eyJ");
    }

    private static URL toImageUrl(JsonNode pathNode) {
        String path = textOrNull(pathNode);
        if (path == null) {
            return null;
        }
        try {
            return URI.create(IMAGE_BASE_URL + path).toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            return null;
        }
    }

    private static Integer toYear(JsonNode releaseDateNode) {
        String releaseDate = textOrNull(releaseDateNode);
        if (releaseDate == null || releaseDate.length() < 4) {
            return null;
        }
        try {
            return Integer.parseInt(releaseDate.substring(0, 4));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean isEmpty(JsonNode node) {
        return textOrNull(node) == null;
    }

    private static String textOrNull(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull() || node.asText().isBlank()) {
            return null;
        }
        return node.asText();
    }
}
