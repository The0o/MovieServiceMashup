package model;

import java.util.Date;

public class VisualisationInfo {

    private Date visualisationDate;
    private int punctuation;

    public VisualisationInfo(Date visualisationDate, int punctuation) {
        setVisualisationDate(visualisationDate);
        setPunctuation(punctuation);
    }

    public Date getVisualisationDate() {
        return visualisationDate;
    }

    public void setVisualisationDate(Date visualisationDate) {
        this.visualisationDate = visualisationDate;
    }

    public int getPunctuation() {
        return punctuation;
    }

    public void setPunctuation(int punctuation) {
        this.punctuation = punctuation;
    }
}
