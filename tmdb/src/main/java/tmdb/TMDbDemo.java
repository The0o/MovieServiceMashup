package tmdb;

import tmdb.client.MovieInformationClientFactory;
import tmdb.exception.MovieInfoNotFoundException;
import tmdb.exception.MovieInfoServiceException;

// Petit programme pour tester le client TMDb : ./gradlew :tmdb:run --args="The Matrix"
public class TMDbDemo {

    public static void main(String[] args) {
        String title = args.length > 0 ? String.join(" ", args) : "The Matrix";
        try {
            System.out.println(MovieInformationClientFactory.getClient().findMovieInformation(title));
        } catch (MovieInfoNotFoundException e) {
            System.err.println("Film introuvable : " + e.getMessage());
        } catch (MovieInfoServiceException | IllegalStateException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}
