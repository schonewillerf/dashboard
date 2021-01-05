package hu.adsd.dashboard.webhooks;

import hu.adsd.dashboard.issue.Issue;
import hu.adsd.dashboard.issue.UpdateItemRepository;
import hu.adsd.dashboard.issue.UpdatedItem;
import hu.adsd.dashboard.jiraClient.JiraClient;
import hu.adsd.dashboard.projectSummary.ProjectSummaryData;
import hu.adsd.dashboard.projectSummary.ProjectSummaryDataRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class WebHookController {

    // list sse emmiters
    public List<SseEmitter> listEmitters=new CopyOnWriteArrayList<>();

    private final ProjectSummaryDataRepository projectSummaryDataRepository;
    private  final UpdateItemRepository updateItemRepository;


    public WebHookController(ProjectSummaryDataRepository projectSummaryDataRepository, UpdateItemRepository updateItemRepository) {
        this.projectSummaryDataRepository = projectSummaryDataRepository;
        this.updateItemRepository = updateItemRepository;
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

            List<UpdatedItem> listUpdatedItems= JiraClient.getUpdates("1w", 7);
            updateItemRepository.deleteAll();
            updateItemRepository.saveAll(listUpdatedItems);



            //dispatch sse emmiter
            for (SseEmitter emitter:listEmitters)
            {
                try {
                    emitter.send(emitter.event().name("webhookJira").data(payload));
                } catch (IOException e) {
                    listEmitters.remove(emitter);
                    //e.printStackTrace();

                }
            }

            return ResponseEntity.ok(payload);
        }




        // if wehbook not recieved
        return ResponseEntity.ok("something went wrong");
    }



    // subscribe emmitter
    @CrossOrigin
    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribeEmmitter()
    {
        // timeout Long.MAX_VALUE
        SseEmitter sseEmitter=new SseEmitter();
        try {
            sseEmitter.send(sseEmitter.event().name("INIT"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sseEmitter.onCompletion(()->listEmitters.remove(sseEmitter));
        listEmitters.add(sseEmitter);

        return sseEmitter;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void subsribeEmmitersAfterStartup() {
        subscribeEmmitter();
    }


}
