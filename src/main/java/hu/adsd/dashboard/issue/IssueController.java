package hu.adsd.dashboard.issue;

import hu.adsd.dashboard.jiraClient.JiraClient;
import hu.adsd.dashboard.jiraClient.ServiceLayerJira;
import hu.adsd.dashboard.projectSummary.ProjectSummaryData;
import hu.adsd.dashboard.projectSummary.ProjectSummaryDataRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class IssueController {
    // Properties
    private final IssueRepository repository;
    private final JiraClient  client;
    private final ProjectSummaryDataRepository projectSummaryDataRepository;
    private  final ServiceLayerJira serviceLayerJira;

    // Constructor
    public IssueController(
            IssueRepository repository,
            JiraClient client,
            ProjectSummaryDataRepository projectSummaryDataRepository,
            ServiceLayerJira serviceLayerJira)
    {
        this.repository = repository;
        this.client = client;
        this.projectSummaryDataRepository = projectSummaryDataRepository;
        this.serviceLayerJira = serviceLayerJira;
    }

    // save all the items from all existing projects of Jira  in db
    @RequestMapping("/save")
    public String saveAllIssuesToCustomerDb()
    {
        // get list with all items from client  and save it to db
        List<Issue> allIssues= JiraClient.getAllIssues();

        for (Issue issue : allIssues)
        {
            repository.save(issue);
        }

        return "data saved!";
    }

    // update the table in project_summary_data,
    // use request like this : http://localhost:8080/updateProjectSummary?status=testing
    @RequestMapping("/updateProjectSummary")
    String update (@RequestParam ("status") String status)
    {
        int storyPoint=0;
        int items=0;

        // get list with all item of testing (doing, done or backlog depending on request parameter)
        List<Issue> list =repository.findByIssueStatusIgnoreCase(status);

        for (Issue issue:list)
        {
            storyPoint+=issue.getStoryPoints();
        }

        items=list.size();

        // update the existing table with data from Api
        ProjectSummaryData projectSummaryData=projectSummaryDataRepository.findOneByName(status);
        projectSummaryData.setStoryPoints(storyPoint);
        projectSummaryData.setItems(items);
        projectSummaryData.setName(status);
        projectSummaryDataRepository.save(projectSummaryData);

        return "issues ' "+status+ "' updated!";
    }

    @RequestMapping("/getUpdatedTasks")
    public List<Issue> getUpdatedTasks()
    {
        List<Issue> issues=new ArrayList<>();
        String[] keys=JiraClient.getkeysOfRecentUpdatedIssues("1w",7);

        for(int i=0; i<keys.length; i++)
        {
            Issue issue = repository.findOneByIssueKeyIgnoreCase(keys[i]);
            issues.add(issue);
        }

        return issues;
    }

    //refresh issue table  by get request
    @GetMapping("/refresh")
    public String refreshPage() {

        // Get all issues from Jira
        List<Issue> allIssues= JiraClient.getAllIssues();

        // Create ArrayList to store issue status totals
        List<ProjectSummaryData> summaryData = new ArrayList<>();

        // Loop over each issue to get totals per category
        for (Issue issue : allIssues) {

            // Create new ProjectSummaryData for current issue
            ProjectSummaryData currentIssueData = new ProjectSummaryData(issue.getIssueStatus());

            // Check if current issue already in summaryData
            if (summaryData.contains(currentIssueData)) {
                currentIssueData = summaryData.get(summaryData.indexOf(currentIssueData));
            }

            // Increment No. of items and storypoints
            currentIssueData.incrementItems();
            currentIssueData.incrementStoryPoints(issue.getStoryPoints());

            summaryData.add(currentIssueData);
        }

        // Save totals to DB
        projectSummaryDataRepository.deleteAll();
        projectSummaryDataRepository.saveAll(summaryData);

        return "db refreshed";
    }
}
