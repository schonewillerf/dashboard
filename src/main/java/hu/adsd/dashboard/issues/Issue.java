package hu.adsd.dashboard.issues;

public class Issue {
    // Properties
    private String issueStatus;
    private int storyPoints;

    // Getters and Setters
    /**
     * Status as in column in ScrumBoard
     * such as: "To Do", "Doing" or "Done"
     *
     * @return String issueStatus
     */
    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }


    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int stroyPoints) {
        this.storyPoints = stroyPoints;
    }
}
