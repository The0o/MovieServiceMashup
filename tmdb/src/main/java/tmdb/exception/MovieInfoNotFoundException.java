package tmdb.exception;

public class MovieInfoNotFoundException extends Exception {

    public MovieInfoNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
