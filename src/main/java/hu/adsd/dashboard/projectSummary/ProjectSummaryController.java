package hu.adsd.dashboard.projectSummary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This controller should be used to generate project summary on front-end
 */
@RestController
public class ProjectSummaryController {
    private final ProjectSummaryDataRepository projectSummaryDataRepository;

    public ProjectSummaryController(ProjectSummaryDataRepository projectSummaryDataRepository) {
        this.projectSummaryDataRepository = projectSummaryDataRepository;
    }

    @GetMapping("/projectsummary")
    public List<ProjectSummaryData> getProjectSummary(){
        return projectSummaryDataRepository.findAll();
    }
}
