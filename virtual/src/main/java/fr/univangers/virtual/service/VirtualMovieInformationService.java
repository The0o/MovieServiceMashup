package fr.univangers.virtual.service;

import fr.univangers.virtual.dto.VirtualServiceMovieDTO;

public interface VirtualMovieInformationService {

    VirtualServiceMovieDTO findMovieInformation(String title);
}
