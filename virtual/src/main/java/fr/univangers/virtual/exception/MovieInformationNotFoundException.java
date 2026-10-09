package fr.univangers.virtual.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//film existant sur tmdb
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieInformationNotFoundException extends VirtualServiceException {

    public MovieInformationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
