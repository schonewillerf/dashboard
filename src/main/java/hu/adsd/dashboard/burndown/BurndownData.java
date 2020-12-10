package hu.adsd.dashboard.burndown;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 *  This will create table in database named burndown_data with PK id
 */
@Entity
public class BurndownData {
    // Properties
    //
    // Adds column id with PK, AI
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //
    // Adds column date, current_quantity
    @Column(columnDefinition = "date")
    private LocalDate date;
    //
    // Adds column current_quantity with default value -1
    @Column(columnDefinition = "integer default -1")
    private int currentQuantity;
    //
    private int estimatedQuantity;

    // Protected Emptyy constructor is required by JPA
    protected BurndownData() { }

    // Other Constructor
    public BurndownData(LocalDate date, int estimatedQuantity) {
        this.date = date;
        this.estimatedQuantity = estimatedQuantity;
        this.currentQuantity = -1;
    }

    // Normal getters, setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
}