package fr.univangers.virtual.exception;

public abstract class VirtualServiceException extends RuntimeException {

    public VirtualServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
