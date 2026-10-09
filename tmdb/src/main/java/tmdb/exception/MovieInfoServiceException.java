package tmdb.exception;

// Erreur technique (TMDb indisponible, cle API invalide...), a distinguer d'un film introuvable
public class MovieInfoServiceException extends RuntimeException {

    public MovieInfoServiceException(String errorMessage) {
        super(errorMessage);
    }

    public MovieInfoServiceException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
