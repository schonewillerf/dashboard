package hu.adsd.dashboard.sentiment;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class DailySentiment
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    int id;

    @Column(columnDefinition = "date")
    private LocalDate date;

    private double averageSentiment;

    protected DailySentiment()
    {
    }

    public DailySentiment( LocalDate date )
    {
        this.date = date;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate( LocalDate date )
    {
        this.date = date;
    }

    public double getAverageSentiment()
    {
        return averageSentiment;
    }

    public void setAverageSentiment( double averageSentiment )
    {
        this.averageSentiment = averageSentiment;
    }
}
