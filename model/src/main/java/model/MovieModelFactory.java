package model;

public class MovieModelFactory {

    private static final MovieModel INSTANCE = new MovieModelImpl();

    private MovieModelFactory() {
    }

    public static MovieModel getModel() {
        return INSTANCE;
    }
}
