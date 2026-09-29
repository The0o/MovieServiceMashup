package virtual.dto;

import tmdb.dto.MovieInfoDto;

// Herite des informations TMDb et ajoute uniquement ce que TMDb ne fournit pas :
// le titre et l'annee existent deja dans MovieInfoDto, ils ne sont pas dupliques
public class VirtualServiceMovieDTO extends MovieInfoDto {

    // null si Walter n'a pas vu le film
    private String visualisationDate;
    private Integer points;

    public VirtualServiceMovieDTO() {
    }

    public VirtualServiceMovieDTO(MovieInfoDto movieInfo, MovieViewingDto viewing) {
        super(movieInfo.getTitle(), movieInfo.getGenres(), movieInfo.getPosterPath(),
                movieInfo.getYear(), movieInfo.getCharacters());
        if (viewing != null) {
            setVisualisationDate(viewing.getVisualisationDate());
            setPoints(viewing.getPoints());
        }
    }

    public String getVisualisationDate() {
        return visualisationDate;
    }

    public void setVisualisationDate(String visualisationDate) {
        this.visualisationDate = visualisationDate;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
