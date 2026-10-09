package fr.univangers.virtual.dto;

import tmdb.dto.MovieInfoDto;

public class VirtualServiceMovieDTO extends MovieInfoDto {

    private boolean seen;
    private String visualisationDate;
    private Integer points;

    public VirtualServiceMovieDTO() {
        super();
    }

    public VirtualServiceMovieDTO(MovieInfoDto movieInfo) {
        super(movieInfo.getTitle(), movieInfo.getGenres(), movieInfo.getPosterPath(), movieInfo.getYear(), movieInfo.getCharacters());
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
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
