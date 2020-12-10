package hu.adsd.dashboard.sentiment;

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

    // Constructor
    public SentimentController(
            SentimentDataRepository sentimentDataRepository,
            DailySentimentRepository dailySentimentRepository
    ) {
        this.sentimentDataRepository = sentimentDataRepository;
        this.dailySentimentRepository = dailySentimentRepository;
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

        return sentimentDataRepository.countSentimentByValue(formattedDate);
    }

    @GetMapping("/sentimentdailychart")
    public List<DailySentiment> getDailySentiment() {

        // Will be dynamic at some point
        LocalDate startDate = LocalDate.parse("2020-11-30");
        LocalDate endDate = LocalDate.parse("2020-12-11");

        return dailySentimentRepository.findAllByDateBetweenOrderByDate( startDate, endDate );
    }
}
