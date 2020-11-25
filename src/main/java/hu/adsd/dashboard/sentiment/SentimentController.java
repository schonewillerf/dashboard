package hu.adsd.dashboard.sentiment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class SentimentController {
    private final SentimentDataRepository sentimentDataRepository;

    public SentimentController(SentimentDataRepository sentimentDataRepository) {
        this.sentimentDataRepository = sentimentDataRepository;
    }

    @GetMapping("/sentimentdata")
    public List<Object[]> getSentimentData() throws ParseException {
        String currentdate = String.valueOf(LocalDate.now());
        Date formattedDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentdate);

        return sentimentDataRepository.countSentimentByValue(formattedDate);
    }

    @PostMapping("/sentimentdata/{vote}")
    public List<Object[]> postSentimentData(@PathVariable int vote) throws ParseException {
        String currentdate = String.valueOf(LocalDate.now());
        Date formattedDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentdate);

        sentimentDataRepository.save(new SentimentData(vote));

        return sentimentDataRepository.countSentimentByValue(formattedDate);
    }
}
