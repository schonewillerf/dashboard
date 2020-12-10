package hu.adsd.dashboard.issue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Issue {
    // Properties
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String issueKey;
    private String issueStatus;
    private String assignedTo;
    private String statusCatogryChangedData;
    private int storyPoints;
    private String projectName;
    private String projectKey;
    private String description;
    private String issueName;

    // Getters and Setters
    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatusCatogryChangedData() {
        return statusCatogryChangedData;
    }

    public void setStatusCatogryChangedData(String statusCatogryChangedData) {
        this.statusCatogryChangedData = statusCatogryChangedData;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int stroyPoints) {
        this.storyPoints = stroyPoints;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }
}
