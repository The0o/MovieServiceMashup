package virtual.client;

import virtual.dto.MovieViewingDto;

import java.util.Optional;

// Isole le service de la technologie utilisee (Thrift) pour recuperer les visionnages de Walter
public interface MovieViewingClient {

    // Optional vide si Walter n'a pas vu le film
    Optional<MovieViewingDto> findMovieViewing(String title);

}
