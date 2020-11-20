package hu.adsd.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private IndicatorRepository indicatorRepository;
    @Autowired
    private BurndownDataRepository burndownDataRepository;

    @CrossOrigin(origins ="*", allowedHeaders ="*")
    @GetMapping("/burndowndata")
    public List<BurndownData> getBurndownData() throws ParseException {
        BurndownData burndownData = new BurndownData();
        Date date2=new SimpleDateFormat("yyyy-MM-dd").parse("20-11-2020");
        burndownData.setDate(date2);
        burndownData.setEstimatedQuantity(10);
        burndownData.setCurrentQuantity(10);
        burndownDataRepository.save(burndownData);

        String sDate1="31-10-2020";
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        return burndownDataRepository.findAllByDateBetweenOrderByDate(date1, new Date());
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

    @CrossOrigin(origins ="*", allowedHeaders ="*")
    @DeleteMapping("/indicator/{id}")
    public List<Indicator> deleteIndicator(@PathVariable int id){
        indicatorRepository.deleteById(id);
        return indicatorRepository.findAll();
    }

}
