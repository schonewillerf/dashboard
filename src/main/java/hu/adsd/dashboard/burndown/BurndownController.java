package hu.adsd.dashboard.burndown;

import hu.adsd.dashboard.employee.EmployeeData;
import hu.adsd.dashboard.employee.EmployeeDataRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class BurndownController {
    private final BurndownDataRepository burndownDataRepository;
    private final EmployeeDataRepository employeeDataRepository;

    public BurndownController(BurndownDataRepository burndownDataRepository, EmployeeDataRepository employeeDataRepository) {
        this.burndownDataRepository = burndownDataRepository;
        this.employeeDataRepository = employeeDataRepository;
    }

    @GetMapping("/burndowndata")
    public List<BurndownData> getBurndownData() throws ParseException {
        LocalDate date1 = LocalDate.parse("2020-11-30");
        LocalDate date2 = LocalDate.parse("2020-12-11");

        return burndownDataRepository.findAllByDateBetweenOrderByDate(date1, date2);
    }

    @GetMapping("/generateestimatedburndowndata")
    public void generateEstimatedBurndownData() throws ParseException {
        List<EmployeeData> allEmployees = employeeDataRepository.findAll();

        String startDateString = "2020-11-30";
        LocalDate startDate = LocalDate.parse(startDateString);
        String endDateString = "2020-12-11";
        LocalDate endDate = LocalDate.parse(endDateString);

        int totalStoryPointsPerSprint = 150;
        int totalWorkingHoursPerSprint = 0;
        int totalWorkingHoursMonday = 0;
        int totalWorkingHoursTuesday = 0;
        int totalWorkingHoursWednesday = 0;
        int totalWorkingHoursThursday = 0;
        int totalWorkingHoursFriday = 0;

        for (EmployeeData employeeData : allEmployees) {
            if (employeeData.isDeveloper()) {
                totalWorkingHoursPerSprint += employeeData.getTotalWorkingHours();

                totalWorkingHoursMonday += employeeData.getWorkingHoursMo();
                totalWorkingHoursTuesday += employeeData.getWorkingHoursTu();
                totalWorkingHoursWednesday += employeeData.getWorkingHoursWe();
                totalWorkingHoursThursday += employeeData.getWorkingHoursTh();
                totalWorkingHoursFriday += employeeData.getWorkingHoursFr();
            }
        }

        double estimatedStoryPointsMonday = ((double) totalWorkingHoursMonday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;
        double estimatedStoryPointsTuesday = ((double) totalWorkingHoursTuesday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;
        double estimatedStoryPointsWednesday = ((double) totalWorkingHoursWednesday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;
        double estimatedStoryPointsThursday = ((double) totalWorkingHoursThursday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;
        double estimatedStoryPointsFriday = ((double) totalWorkingHoursFriday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;

        double[] estimatedStoryPointsPerDayArray = {
                estimatedStoryPointsMonday,
                estimatedStoryPointsTuesday,
                estimatedStoryPointsWednesday,
                estimatedStoryPointsThursday,
                estimatedStoryPointsFriday,
                0,
                0
        };

        List<BurndownData> burndownDataPoints = new ArrayList<>();

        while (startDate.compareTo(endDate) <= 0) {
            int dayOfWeek = startDate.getDayOfWeek().getValue() - 1;
            totalStoryPointsPerSprint -= estimatedStoryPointsPerDayArray[dayOfWeek];

            if (startDate.compareTo(endDate) == 0) {
                totalStoryPointsPerSprint = 0;
            }

            BurndownData burndownData = new BurndownData(startDate, totalStoryPointsPerSprint);

            burndownDataPoints.add(burndownData);

            startDate = startDate.plusDays(1);
        }

        burndownDataRepository.saveAll(burndownDataPoints);

    }
}