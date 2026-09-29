package virtual.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tmdb.exception.MovieInfoNotFoundException;
import virtual.dto.VirtualServiceMovieDTO;
import virtual.service.VirtualMoviesInformationService;

@RestController
@RequestMapping("/movies")
public class VirtualMoviesInformationController {

    private final VirtualMoviesInformationService service;

    public VirtualMoviesInformationController(VirtualMoviesInformationService service) {
        this.service = service;
    }

    // GET /movies/The Matrix
    @GetMapping("/{title}")
    public VirtualServiceMovieDTO findMovieInformation(@PathVariable String title) throws MovieInfoNotFoundException {
        return service.findMovieInformation(title);
    }
}
