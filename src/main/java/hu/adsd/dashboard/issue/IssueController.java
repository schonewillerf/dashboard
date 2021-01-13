package hu.adsd.dashboard.issue;

import hu.adsd.dashboard.jiraClient.JiraClient;
import hu.adsd.dashboard.projectSummary.ProjectSummaryData;
import hu.adsd.dashboard.projectSummary.ProjectSummaryDataRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class IssueController {

    // Properties
    private final IssueRepository repository;
    private final ProjectSummaryDataRepository projectSummaryDataRepository;
    private final UpdateItemRepository updateItemRepository;

    // Constructor
    public IssueController(
        IssueRepository repository, 
        ProjectSummaryDataRepository projectSummaryDataRepository, 
        UpdateItemRepository updateItemRepository
    ) {
        this.repository = repository;
        this.projectSummaryDataRepository = projectSummaryDataRepository;
        this.updateItemRepository = updateItemRepository;
    }

    // save all the items from all existing projects of Jira in db
    @RequestMapping("/save")
    public String saveAllIssuesToCustomerDb() {
        // get list with all items from client and save it to db
        List<Issue> allIssues = JiraClient.getAllIssues();

        for (Issue issue : allIssues) {
            repository.save(issue);
        }

        return "data saved!";
    }

    // update the table in project_summary_data,
    // use request like this :
    // http://localhost:8080/updateProjectSummary?status=testing
    @RequestMapping("/updateProjectSummary")
    String update(@RequestParam("status") String status) {
        int storyPoint = 0;
        int items = 0;

        // get list with all item of testing (doing, done or backlog depending on
        // request parameter)
        List<Issue> list = repository.findByIssueStatusIgnoreCase(status);

        for (Issue issue : list) {
            storyPoint += issue.getStoryPoints();
        }

        items = list.size();

        // update the existing table with data from Api
        ProjectSummaryData projectSummaryData = projectSummaryDataRepository.findOneByName(status);
        projectSummaryData.setStoryPoints(storyPoint);
        projectSummaryData.setItems(items);
        projectSummaryData.setName(status);
        projectSummaryDataRepository.save(projectSummaryData);

        return "issues ' " + status + "' updated!";
    }

    @RequestMapping("/getUpdatedTasks")
    public List<UpdatedItem> getUpdatedTasks() {

        //save updated to db
        List<UpdatedItem> list=JiraClient.getUpdates("1w", 10);
        updateItemRepository.deleteAll();
        updateItemRepository.saveAll(list);
        return list;

    }

    // refresh issue table by get request
    @GetMapping("/refresh")
    public String refreshPage() {

        // Get all issues from Jira
        List<Issue> allIssues = JiraClient.getAllIssues();

        // Create ArrayList to store issue status totals
        List<ProjectSummaryData> summaryData = new ArrayList<>();

        // Loop over each issue to get totals per category
        for (Issue issue : allIssues) {

            // Create new ProjectSummaryData with current issue status e.g. "To Do", "Doing", "Done" etc.
            ProjectSummaryData currentIssueData = new ProjectSummaryData(issue.getIssueStatus());

            // comment needed
            int issueIndex = summaryData.indexOf(currentIssueData);
            if (issueIndex < 0) {
                currentIssueData.increment(issue.getStoryPoints());
                summaryData.add(currentIssueData);
            } else {
                summaryData.get(issueIndex).increment(issue.getStoryPoints());
            }
        }

        // Save totals to DB
        projectSummaryDataRepository.deleteAll();
        projectSummaryDataRepository.saveAll(summaryData);

        return "db refreshed";
    }
}
