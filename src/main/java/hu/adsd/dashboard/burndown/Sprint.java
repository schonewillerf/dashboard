package hu.adsd.dashboard.burndown;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String startDateString;
    private String endDateString;

    public Sprint(String startDateString, String endDateString) {
        this.startDateString = startDateString;
        this.endDateString = endDateString;
    }

    protected Sprint() {}

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDateString() {
        return this.startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getEndDateString() {
        return this.endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }    
}
