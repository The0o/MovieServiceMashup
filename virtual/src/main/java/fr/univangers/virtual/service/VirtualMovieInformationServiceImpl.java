package fr.univangers.virtual.service;

import fr.univangers.movieservice.thrift.MovieDto;
import fr.univangers.virtual.client.MovieCollectionClient;
import fr.univangers.virtual.client.MovieCollectionClientFactory;
import fr.univangers.virtual.dto.VirtualServiceMovieDTO;
import fr.univangers.virtual.exception.MovieInformationNotFoundException;
import fr.univangers.virtual.exception.MovieTitleRequiredException;
import fr.univangers.virtual.exception.UpstreamServiceException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tmdb.client.MovieInformationClient;
import tmdb.client.MovieInformationClientFactory;
import tmdb.dto.MovieInfoDto;
import tmdb.exception.MovieInfoNotFoundException;
import tmdb.exception.MovieInfoServiceException;

import java.util.Optional;

@RestController
public class VirtualMovieInformationServiceImpl implements VirtualMovieInformationService {

    private final MovieInformationClient movieInformationClient = MovieInformationClientFactory.getClient();
    private final MovieCollectionClient movieCollectionClient = MovieCollectionClientFactory.getClient();

    @Override
    @RequestMapping(value = "/api/movies", method = RequestMethod.GET)
    public VirtualServiceMovieDTO findMovieInformation(@RequestParam(name = "title") String title) {
        if (title == null || title.isBlank()) {
            throw new MovieTitleRequiredException("Le titre est obligatoire");
        }

        MovieInfoDto movieInfo = findOnTmdb(title);

        Optional<MovieDto> viewed = movieCollectionClient.findMovieByTitle(movieInfo.getTitle());

        VirtualServiceMovieDTO result = new VirtualServiceMovieDTO(movieInfo);
        viewed.ifPresent(movie -> {
            result.setSeen(true);
            result.setVisualisationDate(movie.getVisualisationDate());
            result.setPoints((int) movie.getPoints());
        });
        return result;
    }

    private MovieInfoDto findOnTmdb(String title) {
        try {
            return movieInformationClient.findMovieInformation(title);
        } catch (MovieInfoNotFoundException e) {
            throw new MovieInformationNotFoundException(e.getMessage(), e);
        } catch (MovieInfoServiceException e) {
            throw new UpstreamServiceException("Erreur du service TMDb : " + e.getMessage(), e);
        }
    }
}
