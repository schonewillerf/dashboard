package hu.adsd.dashboard.jiraClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JiraController {
    // Properties
    private final JiraClient client;

    // Constructor
    public JiraController( JiraClient client )
    {
        this.client = client;
    }

    @GetMapping("/statisticsMethod")
    @ResponseBody
    public int getMethod(@RequestParam("project") String projectName, @RequestParam("task") String taskName)
    {
        // replace whitespace with %20, according to syntax of Rest Api Jira
        //for calls use like this
        // http://localhost:8080/statisticsMethod?project=testProject&task=PRODUCT BACKLOG

        String replacedTaskName=taskName.replace(" ", "%20");

        return client.getStatistics(projectName,replacedTaskName);
    }
}
