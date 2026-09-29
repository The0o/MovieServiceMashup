package virtual.dto;

// Informations de visionnage de Walter, recuperees via le service Thrift
public class MovieViewingDto {

    private String visualisationDate;
    private Integer points;

    public MovieViewingDto() {
    }

    public MovieViewingDto(String visualisationDate, Integer points) {
        setVisualisationDate(visualisationDate);
        setPoints(points);
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
