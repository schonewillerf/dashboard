package hu.adsd.dashboard.sentiment;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class SentimentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    private int value;

    public SentimentData() {
    }

    public SentimentData(int value) throws ParseException {
        String currentdate = String.valueOf(LocalDate.now());
        Date formattedDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentdate);
        this.date = formattedDate;
        this.value = value;
    }

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
