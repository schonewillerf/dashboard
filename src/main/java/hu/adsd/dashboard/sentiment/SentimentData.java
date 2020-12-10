package hu.adsd.dashboard.sentiment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class SentimentData
{
    // Properties
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;
    private LocalDate date;
    private int value;

    // Empty Constructor for JPA
    protected SentimentData() {}

    public SentimentData( int value )
    {
        this.date = LocalDate.now();
        this.value = value;
    }

    // Getters and Setters
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

    public int getValue()
    {
        return value;
    }

    public void setValue( int value )
    {
        this.value = value;
    }
}
