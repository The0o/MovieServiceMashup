package fr.univangers.virtual.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//Service thrift pas joignable
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class MovieCollectionUnavailableException extends VirtualServiceException {

    public MovieCollectionUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
