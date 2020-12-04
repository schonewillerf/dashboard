package hu.adsd.dashboard.sentiment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class SentimentData
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;
    private LocalDate date;
    private int value;

    public SentimentData()
    {
    }

    public SentimentData( int value )
    {
        LocalDate formattedDate = LocalDate.now();
        this.date = formattedDate;
        this.value = value;
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

    public int getValue()
    {
        return value;
    }

    public void setValue( int value )
    {
        this.value = value;
    }
}
