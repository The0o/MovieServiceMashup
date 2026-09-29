package virtual.service;

import tmdb.exception.MovieInfoNotFoundException;
import virtual.dto.VirtualServiceMovieDTO;

public interface VirtualMoviesInformationService {

    VirtualServiceMovieDTO findMovieInformation(String title) throws MovieInfoNotFoundException;

}
