package hu.adsd.dashboard.burndown;

import hu.adsd.dashboard.sentiment.DailySentiment;
import hu.adsd.dashboard.sentiment.DailySentimentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class BurndownController {
    // Repositories
    private final BurndownDataRepository burndownDataRepository;
    private final DailySentimentRepository dailySentimentRepository;
    private final SprintRepository sprintRepository;

    // Spring Boot will automatically inject these repositories at initialisation
    public BurndownController(
            BurndownDataRepository burndownDataRepository,
            DailySentimentRepository dailySentimentRepository,
            SprintRepository sprintRepository
    ) {
        this.burndownDataRepository = burndownDataRepository;
        this.dailySentimentRepository = dailySentimentRepository;
        this.sprintRepository = sprintRepository;
    }

    @GetMapping("/burndowndata")
    public Object[] getBurndownData() {
        
        Sprint currentSprint = sprintRepository.findAll().get(0);

        LocalDate startDate = LocalDate.parse(currentSprint.getStartDateString());
        LocalDate endDate = LocalDate.parse(currentSprint.getEndDateString());
        
        List<BurndownData> sprintBurndown = burndownDataRepository.findAllByDateBetweenOrderByDate(startDate, endDate);
        List<DailySentiment> sprintSentiment = dailySentimentRepository.findAllByDateBetweenOrderByDate(startDate, endDate);

        // Prepare JSON response object consisting of two arrays
        Object[] jsonResponse = new Object[2];
        jsonResponse[0] = sprintBurndown;
        jsonResponse[1] = sprintSentiment;

        return jsonResponse;
    }
}