package hu.adsd.dashboard.webhooks;

import hu.adsd.dashboard.burndown.BurndownGenerator;
import hu.adsd.dashboard.burndown.Sprint;
import hu.adsd.dashboard.burndown.SprintRepository;
import hu.adsd.dashboard.issue.Issue;
import hu.adsd.dashboard.issue.UpdateItemRepository;
import hu.adsd.dashboard.issue.UpdatedItem;
import hu.adsd.dashboard.jiraClient.JiraClient;
import hu.adsd.dashboard.messenger.MessageService;
import hu.adsd.dashboard.projectSummary.ProjectSummaryData;
import hu.adsd.dashboard.projectSummary.ProjectSummaryDataRepository;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/webhook")
@RestController
public class WebHookController {

    private final ProjectSummaryDataRepository projectSummaryDataRepository;
    private final UpdateItemRepository updateItemRepository;
    private final SprintRepository sprintRepository;
    private final MessageService messageService;
    private final BurndownGenerator burndownGenerator;

    public WebHookController(
        ProjectSummaryDataRepository projectSummaryDataRepository, 
        UpdateItemRepository updateItemRepository,
        SprintRepository sprintRepository,
        MessageService messageService,
        BurndownGenerator burndownGenerator
    ) {
        this.projectSummaryDataRepository = projectSummaryDataRepository;
        this.updateItemRepository = updateItemRepository;
        this.sprintRepository = sprintRepository;
        this.messageService = messageService;
        this.burndownGenerator = burndownGenerator;
    }

    @PostMapping("/sprint")
    public void sprintListener() {

        // Call Api to get current sprint days
        Sprint currentSprint = JiraClient.getCurrentSprint();

        // Save current sprintdays
        Sprint sprintToBeUpdated = sprintRepository.findAllByJiraId(currentSprint.getJiraId());
        if (sprintToBeUpdated == null){
            sprintRepository.save(currentSprint);
        } else {
            sprintToBeUpdated.setEndDateString(currentSprint.getEndDateString());
            sprintToBeUpdated.setStartDateString(currentSprint.getStartDateString());
            sprintRepository.save(sprintToBeUpdated);
        }

        // Recalculate expected burndown
        burndownGenerator.generateEstimatedBurndownData(currentSprint);
    }

    @PostMapping("/issue")
    public void webHookListener() {

        List<Issue> allIssues = JiraClient.getAllIssues();
        List<ProjectSummaryData> summaryDataList = new ArrayList<>();

        // Loop over issues and increment items and storyPoint for respective projectSummary
        //
        // Inspired by the MutableInt method from: 
        // https://stackoverflow.com/questions/81346/most-efficient-way-to-increment-a-map-value-in-java
        //
        for (Issue issue : allIssues) {

            ProjectSummaryData currentIssueData = new ProjectSummaryData(issue.getIssueStatus());

            // returns the index of the first occurence or -1 if there are none
            int issueIndex = summaryDataList.indexOf(currentIssueData);

            if (issueIndex < 0) {
                currentIssueData.increment(issue.getStoryPoints());
                summaryDataList.add(currentIssueData);
            } else {
                summaryDataList.get(issueIndex).increment(issue.getStoryPoints());
            }
        }

        // Save totals to DB
        projectSummaryDataRepository.deleteAll();
        projectSummaryDataRepository.saveAll(summaryDataList);

        //update tasks
        List<UpdatedItem> listUpdatedItems= JiraClient.getUpdates("1w", 10);
        updateItemRepository.deleteAll();
        updateItemRepository.saveAll(listUpdatedItems);

        //Send message to clients
        messageService.sendMessage("updateScrumboard");
    }
}
