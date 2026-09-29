package virtual.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tmdb.dto.MovieInfoDto;
import tmdb.exception.MovieInfoNotFoundException;
import tmdb.exception.MovieInfoServiceException;
import virtual.dto.MovieViewingDto;
import virtual.dto.VirtualServiceMovieDTO;
import virtual.exception.MovieViewingServiceException;
import virtual.service.VirtualMoviesInformationService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VirtualMoviesInformationController.class)
class VirtualMoviesInformationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VirtualMoviesInformationService service;

    @Test
    void returns200WithCombinedMovie() throws Exception {
        MovieInfoDto info = new MovieInfoDto("The Matrix", List.of("Action"), null, 1999, List.of());
        when(service.findMovieInformation("The Matrix"))
                .thenReturn(new VirtualServiceMovieDTO(info, new MovieViewingDto("2025-03-01", 8)));

        mockMvc.perform(get("/movies/{title}", "The Matrix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Matrix"))
                .andExpect(jsonPath("$.year").value(1999))
                .andExpect(jsonPath("$.visualisationDate").value("2025-03-01"))
                .andExpect(jsonPath("$.points").value(8));
    }

    @Test
    void returns404WhenMovieIsUnknown() throws Exception {
        when(service.findMovieInformation("Inconnu")).thenThrow(new MovieInfoNotFoundException("introuvable"));
        mockMvc.perform(get("/movies/Inconnu")).andExpect(status().isNotFound());
    }

    @Test
    void returns400WhenTitleIsBlank() throws Exception {
        when(service.findMovieInformation(" ")).thenThrow(new IllegalArgumentException("Le titre est obligatoire"));
        mockMvc.perform(get("/movies/{title}", " ")).andExpect(status().isBadRequest());
    }

    @Test
    void returns502WhenTmdbFails() throws Exception {
        when(service.findMovieInformation("The Matrix")).thenThrow(new MovieInfoServiceException("Cle API TMDb invalide"));
        mockMvc.perform(get("/movies/{title}", "The Matrix")).andExpect(status().isBadGateway());
    }

    @Test
    void returns503WhenThriftIsUnavailable() throws Exception {
        when(service.findMovieInformation("The Matrix"))
                .thenThrow(new MovieViewingServiceException("Service Thrift injoignable", null));
        mockMvc.perform(get("/movies/{title}", "The Matrix")).andExpect(status().isServiceUnavailable());
    }
}
