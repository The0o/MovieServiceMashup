package virtual.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tmdb.exception.MovieInfoNotFoundException;
import tmdb.exception.MovieInfoServiceException;
import virtual.exception.MovieViewingServiceException;

// Traduction des exceptions en codes HTTP
@RestControllerAdvice
public class RestExceptionHandler {

    // Titre vide ou invalide : erreur du client
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleBadRequest(IllegalArgumentException e) {
        return problem(HttpStatus.BAD_REQUEST, "Requete invalide", e.getMessage());
    }

    // Film inconnu de TMDb
    @ExceptionHandler(MovieInfoNotFoundException.class)
    public ProblemDetail handleNotFound(MovieInfoNotFoundException e) {
        return problem(HttpStatus.NOT_FOUND, "Film introuvable", e.getMessage());
    }

    // TMDb a renvoye une erreur (cle invalide, quota depasse...) : le service amont a mal repondu
    @ExceptionHandler(MovieInfoServiceException.class)
    public ProblemDetail handleTmdbError(MovieInfoServiceException e) {
        return problem(HttpStatus.BAD_GATEWAY, "Erreur du service TMDb", e.getMessage());
    }

    // Le serveur Thrift ne repond pas : service temporairement indisponible
    @ExceptionHandler(MovieViewingServiceException.class)
    public ProblemDetail handleThriftUnavailable(MovieViewingServiceException e) {
        return problem(HttpStatus.SERVICE_UNAVAILABLE, "Service Thrift indisponible", e.getMessage());
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        return problem;
    }
}
