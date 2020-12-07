package hu.adsd.dashboard.issue;

import hu.adsd.dashboard.jiraClient.JiraClient;
import hu.adsd.dashboard.projectSummary.ProjectSummaryData;
import hu.adsd.dashboard.projectSummary.ProjectSummaryDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class IssueController {

    @Autowired
    IssueRepository repository;

    @Autowired
    JiraClient  client;
    @Autowired
    ProjectSummaryDataRepository projectSummaryDataRepository;


    // save alll the items from all existing projects of Jira  in db
    @RequestMapping("/save")

    public String saveAllIssuesToCustomerDb()
    {
        // get list with all items from client  and save it to db

        List<Issue> allissues= JiraClient.getAllIssues();

        for (Issue issue:allissues)
        {
            repository.save(issue);
        }
        return "data saved!";
    }


    // update the table in project_summary_data, use request like this : http://localhost:8080/updateProjectSummary?status=testing
    @RequestMapping("/updateProjectSummary")
    String upadte (@RequestParam ("status") String status)

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
        String[] keys=JiraClient.getkeysOfRecentUpdatedIssues();
        System.out.println("length?"+ keys.length);

        for(int i=0; i<keys.length; i++)
        {
            Issue issue=findIssueByKey(keys[i]);
            issues.add(issue);
        }

        return issues;
    }
    //find item by key

    public Issue findIssueByKey(String key)
    {
        return repository.findOneByIssueKeyIgnoreCase(key);
    }


    //refresh issue table by server start

    @EventListener(ApplicationReadyEvent.class)
    public void refreshDataAfterStartup()
    {
        repository.deleteAll();
        saveAllIssuesToCustomerDb();
        System.out.println("data refreshed!");
    }





}
