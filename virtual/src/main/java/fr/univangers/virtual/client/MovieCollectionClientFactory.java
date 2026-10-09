package fr.univangers.virtual.client;

public final class MovieCollectionClientFactory {

    public static final String URL = "http://localhost:8080/thrift/MovieService";

    private static final MovieCollectionClient INSTANCE = new ThriftMovieCollectionClient(URL);

    private MovieCollectionClientFactory() {
    }

    public static MovieCollectionClient getClient() {
        return INSTANCE;
    }
}
