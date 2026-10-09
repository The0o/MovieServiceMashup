package fr.univangers.virtual.client;

import fr.univangers.movieservice.thrift.MovieDto;

import java.util.Optional;

public interface MovieCollectionClient {

    Optional<MovieDto> findMovieByTitle(String title);

}
