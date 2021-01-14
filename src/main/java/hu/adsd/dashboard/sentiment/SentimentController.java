package hu.adsd.dashboard.sentiment;

import hu.adsd.dashboard.burndown.Sprint;
import hu.adsd.dashboard.burndown.SprintRepository;
import hu.adsd.dashboard.messages.MessagesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class SentimentController {
    // Properties
    private final SentimentDataRepository sentimentDataRepository;
    private final DailySentimentRepository dailySentimentRepository;
    private final SprintRepository sprintRepository;
    private final MessagesService messagesService;

    // Constructor
    public SentimentController(
            SentimentDataRepository sentimentDataRepository,
            DailySentimentRepository dailySentimentRepository,
            SprintRepository sprintRepository,
            MessagesService messagesService
        ) {
        this.sentimentDataRepository = sentimentDataRepository;
        this.dailySentimentRepository = dailySentimentRepository;
        this.sprintRepository = sprintRepository;
        this.messagesService = messagesService;
    }

    @GetMapping("/sentimentdata")
    public List<Object[]> getSentimentData() {
        LocalDate formattedDate = LocalDate.now();

        return sentimentDataRepository.countSentimentByValue(formattedDate);
    }

    @PostMapping("/sentimentdata/{vote}")
    public List<Object[]> postSentimentData(@PathVariable int vote) {
        LocalDate formattedDate = LocalDate.now();

        sentimentDataRepository.save(new SentimentData(vote));

        // Update average vote in dailySentiment
        double averageSentiment = sentimentDataRepository.calculateAverageSentiment( formattedDate );

        // Use Optional with DailySentiment
        // Shorter syntax
        DailySentiment dailySentiment = dailySentimentRepository
                .findByDate( formattedDate )
                .orElse( new DailySentiment(formattedDate) );

        dailySentiment.setAverageSentiment( averageSentiment );
        dailySentimentRepository.save( dailySentiment );

        messagesService.sendMessage("updateSentiment");

        return sentimentDataRepository.countSentimentByValue(formattedDate);
    }

    @GetMapping("/sentimentdailychart")
    public List<DailySentiment> getDailySentiment() {

        Sprint currentSprint = sprintRepository.findAll().get(0);

        LocalDate startDate = LocalDate.parse(currentSprint.getStartDateString());
        LocalDate endDate = LocalDate.parse(currentSprint.getEndDateString());

        return dailySentimentRepository.findAllByDateBetweenOrderByDate( startDate, endDate );
    }
}
