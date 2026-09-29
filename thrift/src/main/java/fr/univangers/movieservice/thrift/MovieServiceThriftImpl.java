package fr.univangers.movieservice.thrift;

import org.apache.thrift.TException;

import java.util.List;

public class MovieServiceThriftImpl implements MovieService.Iface {
    @Override
    public void addMovie(MovieDto movie) throws TException {

    }

    @Override
    public MovieDto findMovieByTitle(String title) throws ServiceMovieNotFoundException, TException {
        return null;
    }

    @Override
    public List<MovieDto> findMoviesByYear(short year) throws TException {
        return List.of();
    }
}
