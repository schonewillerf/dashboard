package hu.adsd.dashboard.burndown;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class BurndownController {
    private final BurndownDataRepository burndownDataRepository;

    public BurndownController(BurndownDataRepository burndownDataRepository) {
        this.burndownDataRepository = burndownDataRepository;
    }

    @GetMapping("/burndowndata")
    public List<BurndownData> getBurndownData() throws ParseException {
        String sDate1="2020-10-31";
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        String sDate2="2020-11-28";
        Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(sDate2);

        return burndownDataRepository.findAllByDateBetweenOrderByDate(date1, date2);
    }
}