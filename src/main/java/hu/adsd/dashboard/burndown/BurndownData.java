package hu.adsd.dashboard.burndown;

import javax.persistence.*;
import java.util.Date;

@Entity
public class BurndownData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    private int currentQuantity;

    @Column(columnDefinition = "integer default -1")
    private int estimatedQuantity;

    @Column(columnDefinition = "double default -1")
    private double sentimentScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public int getEstimatedQuantity() {
        return estimatedQuantity;
    }

    public void setEstimatedQuantity(int estimatedQuantity) {
        this.estimatedQuantity = estimatedQuantity;
    }

    public double getSentimentScore()
    {
        return sentimentScore;
    }

    public void setSentimentScore( double sentimentScore )
    {
        this.sentimentScore = sentimentScore;
    }
}