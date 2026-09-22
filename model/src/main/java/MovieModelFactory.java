public class MovieModelFactory {

    private static MovieModelImpl instance = new MovieModelImpl();

    private MovieModelFactory() {

    }

    public MovieModelImpl getInstance() {
        if (instance == null) {
            instance = new MovieModelImpl();
        }
        else {
            return instance;
        }
        return instance;
    }
}
