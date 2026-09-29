package virtual.service;

import org.springframework.stereotype.Service;
import tmdb.client.MovieInformationClient;
import tmdb.dto.MovieInfoDto;
import tmdb.exception.MovieInfoNotFoundException;
import virtual.client.MovieViewingClient;
import virtual.dto.MovieViewingDto;
import virtual.dto.VirtualServiceMovieDTO;

import java.util.Optional;

// Facade qui combine le client TMDb (exo 3) et le client Thrift (exo 2)
@Service
public class VirtualMoviesInformationServiceImpl implements VirtualMoviesInformationService {

    private final MovieInformationClient movieInformationClient;
    private final MovieViewingClient movieViewingClient;

    public VirtualMoviesInformationServiceImpl(MovieInformationClient movieInformationClient,
                                               MovieViewingClient movieViewingClient) {
        this.movieInformationClient = movieInformationClient;
        this.movieViewingClient = movieViewingClient;
    }

    @Override
    public VirtualServiceMovieDTO findMovieInformation(String title) throws MovieInfoNotFoundException {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }

        MovieInfoDto movieInfo = movieInformationClient.findMovieInformation(title.trim());

        // On cherche d'abord avec le titre saisi, puis avec le titre officiel TMDb (ex : "amelie" -> "Amélie")
        Optional<MovieViewingDto> viewing = movieViewingClient.findMovieViewing(title.trim());
        if (viewing.isEmpty() && !movieInfo.getTitle().equalsIgnoreCase(title.trim())) {
            viewing = movieViewingClient.findMovieViewing(movieInfo.getTitle());
        }

        return new VirtualServiceMovieDTO(movieInfo, viewing.orElse(null));
    }
}
