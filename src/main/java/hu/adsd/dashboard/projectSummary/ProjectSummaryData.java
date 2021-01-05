package hu.adsd.dashboard.projectSummary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProjectSummaryData {
    // Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int storyPoints;
    private int items;

    // Protected Constructor for JPA
    protected ProjectSummaryData() {}

    // Custom Constructor
    public ProjectSummaryData(String name) {
        this.name = name;
    }

    // Increment items and storyPoints
    public void increment(int storyPoints) {
        this.storyPoints += storyPoints;
        this.items += 1;
    }

    // Compare Data for Equality
    //
    // Objects with same name are equal
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if ((obj instanceof ProjectSummaryData) == false) return false;

        return this.name.equals(((ProjectSummaryData) obj).getName());
    }
    //
    // Should implement hashCode() if equals() is implemented
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    // Increment Methods
    public void incrementItems() {
        this.items += 1;
    }

    public void incrementStoryPoints(int storyPoints2) {
        this.storyPoints += storyPoints2;
    }

}
