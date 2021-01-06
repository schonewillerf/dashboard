package hu.adsd.dashboard.webhooks;

import hu.adsd.dashboard.issue.Issue;
import hu.adsd.dashboard.issue.UpdateItemRepository;
import hu.adsd.dashboard.issue.UpdatedItem;
import hu.adsd.dashboard.jiraClient.JiraClient;
import hu.adsd.dashboard.messenger.MessageService;
import hu.adsd.dashboard.projectSummary.ProjectSummaryData;
import hu.adsd.dashboard.projectSummary.ProjectSummaryDataRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WebHookController {

    private final ProjectSummaryDataRepository projectSummaryDataRepository;
    private final UpdateItemRepository updateItemRepository;
    private final MessageService messageService;

    public WebHookController(ProjectSummaryDataRepository projectSummaryDataRepository, UpdateItemRepository updateItemRepository, MessageService messageService) {
        this.projectSummaryDataRepository = projectSummaryDataRepository;
        this.updateItemRepository = updateItemRepository;
        this.messageService = messageService;
    }

    @PostMapping("/webhook")
    ResponseEntity<String> webHookListener (@RequestBody String payload)
    {
        if (payload!=null)
        {
            // refresh data
            List<Issue> allIssues = JiraClient.getAllIssues();
            List<ProjectSummaryData> summaryData = new ArrayList<>();
            for (Issue issue : allIssues) {
                System.out.println(issue.getIssueStatus());
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

            //updated tasks

            List<UpdatedItem> listUpdatedItems= JiraClient.getUpdates("1w", 10);
            updateItemRepository.deleteAll();
            updateItemRepository.saveAll(listUpdatedItems);

            //Send message to clients
            messageService.sendMessage("updateScrumboard");

            return ResponseEntity.ok(payload);
        }

        // if webhook not received
        return ResponseEntity.ok("something went wrong");
    }
}
