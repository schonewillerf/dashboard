package hu.adsd.dashboard.jiraClient;

import hu.adsd.dashboard.issue.Issue;
import hu.adsd.dashboard.issue.IssueRepository;
import hu.adsd.dashboard.projectSummary.ProjectSummaryData;
import hu.adsd.dashboard.projectSummary.ProjectSummaryDataRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayerJira {


    private final ProjectSummaryDataRepository projectSummaryDataRepository;
    private final IssueRepository issueRepository;

    public ServiceLayerJira( ProjectSummaryDataRepository projectSummaryDataRepository, IssueRepository issueRepository) {
        this.projectSummaryDataRepository = projectSummaryDataRepository;
        this.issueRepository = issueRepository;
    }


    public String upadteProjectSummaryData(ArrayList arrayStatusToUpdate)
    {
        projectSummaryDataRepository.deleteAll();
        List<String> arrayStatus= arrayStatusToUpdate;
        //update existing table with summary data
        for (String varStatus : arrayStatus)
        {
            int storyPoint=0;
            int items=0;
            List<Issue> list =issueRepository.findByIssueStatusIgnoreCase(varStatus);
            items=list.size();
            for (Issue issue:list)
            { storyPoint+=issue.getStoryPoints();
            }
            ProjectSummaryData projectSummaryData=new ProjectSummaryData();
            projectSummaryData.setStoryPoints(storyPoint);
            projectSummaryData.setItems(items);
            projectSummaryData.setName(varStatus);

            projectSummaryDataRepository.save(projectSummaryData);
        }
        return "issues updated!";

    }
}
