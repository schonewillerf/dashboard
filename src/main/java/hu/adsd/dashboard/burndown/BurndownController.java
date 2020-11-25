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
        String sDate1="31-10-2020";
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);

        return burndownDataRepository.findAllByDateBetweenOrderByDate(date1, new Date());
    }
}