package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    private String title;
    private int year;
    private VisualisationInfo visualisationInfo;

    public Movie() {
    }

    public Movie(String title, int year, Date visualisationDate, int puntuation) {
        setTitle(title);
        setYear(year);
        setVisualisationInfo(new VisualisationInfo(visualisationDate, puntuation));
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public VisualisationInfo getVisualisationInfo() {
        return visualisationInfo;
    }

    public void setVisualisationInfo(VisualisationInfo visualisationInfo) {
        this.visualisationInfo = visualisationInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
