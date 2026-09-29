package virtual.exception;

// Le service Thrift est injoignable ou a renvoye une erreur technique
public class MovieViewingServiceException extends RuntimeException {

    public MovieViewingServiceException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
