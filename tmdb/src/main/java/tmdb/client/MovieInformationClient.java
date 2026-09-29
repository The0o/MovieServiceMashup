package tmdb.client;

import tmdb.dto.MovieInfoDto;
import tmdb.exception.MovieInfoNotFoundException;

public interface MovieInformationClient {

    MovieInfoDto findMovieInformation(String title) throws MovieInfoNotFoundException;

}
