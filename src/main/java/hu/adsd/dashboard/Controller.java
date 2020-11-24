package hu.adsd.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private IndicatorRepository indicatorRepository;
    @Autowired
    private BurndownDataRepository burndownDataRepository;
    @Autowired
    private SentimentDataRepository sentimentDataRepository;
    @Autowired
    private TopContributorDataRepository topContributorDataRepository;

    @GetMapping("/burndowndata")
    public List<BurndownData> getBurndownData() throws ParseException {
        String sDate1="31-10-2020";
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);

        return burndownDataRepository.findAllByDateBetweenOrderByDate(date1, new Date());
    }

    @GetMapping("/sentimentdata")
    public List<Object[]> getSentimentData() throws ParseException {
        String currentdate = String.valueOf(LocalDate.now());
        Date formattedDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentdate);

        return sentimentDataRepository.countSentimentByValue(formattedDate);
    }

    @CrossOrigin(origins ="*", allowedHeaders ="*")
    @PostMapping("/sentimentdata/{vote}")
    public List<Object[]> postSentimentData(@PathVariable int vote) throws ParseException {
        String currentdate = String.valueOf(LocalDate.now());
        Date formattedDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentdate);

        sentimentDataRepository.save(new SentimentData(vote));

        return sentimentDataRepository.countSentimentByValue(formattedDate);
    }

    @CrossOrigin(origins ="*", allowedHeaders ="*")
    @GetMapping("/topcontributordata")
    public List<TopContributorData> getTopContributorData() throws ParseException {
        return topContributorDataRepository.findTop5ByOrderByStoryPointsDesc();
    }

    @GetMapping("/indicator")
    public List<Indicator> getIndicators(){
        return indicatorRepository.findAll();
    }

    @GetMapping("/indicator/{id}")
    public Indicator getIndicator(@PathVariable int id){
        return indicatorRepository.findById(id).orElseThrow();
    }

    @PostMapping("/indicator")
    public List<Indicator> addIndicator(@RequestBody Indicator indicator){
        indicatorRepository.save(indicator);
        return indicatorRepository.findAll();
    }

    @PutMapping("/indicator")
    public List<Indicator> updateIndicator(@RequestBody Indicator indicator){
        indicatorRepository.findById(indicator.getId()).orElseThrow();
        indicatorRepository.save(indicator);

        return indicatorRepository.findAll();
    }

    @DeleteMapping("/indicator/{id}")
    public List<Indicator> deleteIndicator(@PathVariable int id){
        indicatorRepository.deleteById(id);

        return indicatorRepository.findAll();
    }

}
