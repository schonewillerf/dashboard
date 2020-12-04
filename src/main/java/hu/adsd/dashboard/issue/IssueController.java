package hu.adsd.dashboard.issue;

import hu.adsd.dashboard.jiraClient.JiraClient;
import hu.adsd.dashboard.projectSummary.ProjectSummaryData;
import hu.adsd.dashboard.projectSummary.ProjectSummaryDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/save")
    @ResponseBody

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


    // update the table in project_summary_data, use request like this : http://localhost:8080/update?status=testing
    @GetMapping("/update")
    @ResponseBody
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

        List<ProjectSummaryData> psdFromDb = projectSummaryDataRepository.findByName(status);
        ProjectSummaryData objFromDb=psdFromDb.get(0);
        objFromDb.setStoryPoints(storyPoint);
        objFromDb.setItems(items);
        objFromDb.setName(status);
        projectSummaryDataRepository.save(objFromDb);

        return "issues ' "+status+ "' updated!";

    }



}
