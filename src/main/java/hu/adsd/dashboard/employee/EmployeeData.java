package hu.adsd.dashboard.employee;

import javax.persistence.*;
import java.util.Date;

/**
 *  This will create table in database named employee_data with PK id
 */
@Entity
public class EmployeeData {
    // Adds column id with PK, AI
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Adds column name, birthday, is_developer
    private String name;

    @Column(columnDefinition = "date")
    private Date birthday;

    private boolean isDeveloper;

    // Adds column working_hours for each day
    private int workingHoursMo;
    private int workingHoursTu;
    private int workingHoursWe;
    private int workingHoursTh;
    private int workingHoursFr;

    // Intentionally left empty
    protected EmployeeData() { }

    // Normal getters, setters
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public void setDeveloper(boolean developer) {
        isDeveloper = developer;
    }

    public int getWorkingHoursMo() {
        return workingHoursMo;
    }

    public void setWorkingHoursMo(int workingHoursMo) {
        this.workingHoursMo = workingHoursMo;
    }

    public int getWorkingHoursTu() {
        return workingHoursTu;
    }

    public void setWorkingHoursTu(int workingHoursTu) {
        this.workingHoursTu = workingHoursTu;
    }

    public int getWorkingHoursWe() {
        return workingHoursWe;
    }

    public void setWorkingHoursWe(int workingHoursWe) {
        this.workingHoursWe = workingHoursWe;
    }

    public int getWorkingHoursTh() {
        return workingHoursTh;
    }

    public void setWorkingHoursTh(int workingHoursTh) {
        this.workingHoursTh = workingHoursTh;
    }

    public int getWorkingHoursFr() {
        return workingHoursFr;
    }

    public void setWorkingHoursFr(int workingHoursFr) {
        this.workingHoursFr = workingHoursFr;
    }

}
