package exception;

public class MovieNotFoundException extends Exception {

    public MovieNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
