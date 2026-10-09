package fr.univangers.virtual.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//pas de titre
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MovieTitleRequiredException extends VirtualServiceException {

    public MovieTitleRequiredException(String message) {
        super(message, null);
    }
}
