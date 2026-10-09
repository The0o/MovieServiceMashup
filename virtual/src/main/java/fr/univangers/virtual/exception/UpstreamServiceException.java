package fr.univangers.virtual.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//erreur de tmdb (impossible de savoir plus precisement les erreurs donnees par tmdb, donc on generalise)
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class UpstreamServiceException extends VirtualServiceException {

    public UpstreamServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
