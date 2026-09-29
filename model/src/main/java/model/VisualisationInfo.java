package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisualisationInfo {

    @JsonProperty("date")
    private LocalDate visualisationDate;
    private int punctuation;

    public VisualisationInfo() {
    }

    public VisualisationInfo(LocalDate visualisationDate, int punctuation) {
        setVisualisationDate(visualisationDate);
        setPunctuation(punctuation);
    }

    public LocalDate getVisualisationDate() {
        return visualisationDate;
    }

    public void setVisualisationDate(LocalDate visualisationDate) {
        this.visualisationDate = visualisationDate;
    }

    public int getPunctuation() {
        return punctuation;
    }

    public void setPunctuation(int punctuation) {
        this.punctuation = punctuation;
    }

    // Le JSON contient une note decimale (ex : 6.7), on l'arrondit a l'entier le plus proche
    @JsonSetter("rating")
    private void setRating(double rating) {
        this.punctuation = (int) Math.round(rating);
    }
}
