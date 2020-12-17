package hu.adsd.dashboard.webhooks;

import hu.adsd.dashboard.issue.Issue;
import hu.adsd.dashboard.issue.IssueRepository;
import hu.adsd.dashboard.jiraClient.JiraClient;
import hu.adsd.dashboard.projectSummary.ProjectSummaryData;
import hu.adsd.dashboard.projectSummary.ProjectSummaryDataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WebHookListener {

    private final ProjectSummaryDataRepository projectSummaryDataRepository;
    private final IssueRepository issueRepository;

    public WebHookListener(ProjectSummaryDataRepository projectSummaryDataRepository, IssueRepository issueRepository) {
        this.projectSummaryDataRepository = projectSummaryDataRepository;
        this.issueRepository = issueRepository;
    }


    @PostMapping("/webhook")
    ResponseEntity<String> listen (@RequestBody String payload)
    {
        if (payload!=null)
        {

            List<Issue> allIssues = JiraClient.getAllIssues();
            List<ProjectSummaryData> summaryData = new ArrayList<>();
            for (Issue issue : allIssues) {

                ProjectSummaryData currentIssueData = new ProjectSummaryData(issue.getIssueStatus());
                if (summaryData.contains(currentIssueData)) {
                    currentIssueData = summaryData.get(summaryData.indexOf(currentIssueData));
                }
                currentIssueData.incrementItems();
                currentIssueData.incrementStoryPoints(issue.getStoryPoints());
                summaryData.add(currentIssueData);
            }

            // Save totals to DB
            projectSummaryDataRepository.deleteAll();
            projectSummaryDataRepository.saveAll(summaryData);

            // for future would be changed to table with updates
            issueRepository.deleteAll();
            issueRepository.saveAll(allIssues);

            return ResponseEntity.ok(payload);
        }

        //ResponseEntity.notFound
        return ResponseEntity.ok("something went wrong");
    }

}
