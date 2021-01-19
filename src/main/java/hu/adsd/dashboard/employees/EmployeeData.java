package hu.adsd.dashboard.employees;

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
    private int workingHoursSa;
    private int workingHoursSu;
    private int workingHoursMo2;
    private int workingHoursTu2;
    private int workingHoursWe2;
    private int workingHoursTh2;
    private int workingHoursFr2;


    // Intentionally left empty
    protected EmployeeData() { }

    // Other constructor
    public EmployeeData(String name) {
        this.name = name;
    }

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

    public int getWorkingHoursSa() {
        return workingHoursSa;
    }

    public void setWorkingHoursSa(int workingHoursSa) {
        this.workingHoursSa = workingHoursSa;
    }

    public int getWorkingHoursSu() {
        return workingHoursSu;
    }

    public void setWorkingHoursSu(int workingHoursSu) {
        this.workingHoursSu = workingHoursSu;
    }

    public int getWorkingHoursMo2() {
        return workingHoursMo2;
    }

    public void setWorkingHoursMo2(int workingHoursMo2) {
        this.workingHoursMo2 = workingHoursMo2;
    }

    public int getWorkingHoursTu2() {
        return workingHoursTu2;
    }

    public void setWorkingHoursTu2(int workingHoursTu2) {
        this.workingHoursTu2 = workingHoursTu2;
    }

    public int getWorkingHoursWe2() {
        return workingHoursWe2;
    }

    public void setWorkingHoursWe2(int workingHoursWe2) {
        this.workingHoursWe2 = workingHoursWe2;
    }

    public int getWorkingHoursTh2() {
        return workingHoursTh2;
    }

    public void setWorkingHoursTh2(int workingHoursTh2) {
        this.workingHoursTh2 = workingHoursTh2;
    }

    public int getWorkingHoursFr2() {
        return workingHoursFr2;
    }

    public void setWorkingHoursFr2(int workingHoursFr2) {
        this.workingHoursFr2 = workingHoursFr2;
    }
}
