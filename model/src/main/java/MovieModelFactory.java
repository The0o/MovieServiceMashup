public class MovieModelFactory {

    private static MovieModelImpl instance = new MovieModelImpl();

    private MovieModelFactory() {

    }

    public MovieModel getModel() {
        if (instance == null) {
            instance = new MovieModelImpl();
        }
        else {
            return instance;
        }
        return instance;
    }
}
