package hu.adsd.dashboard.burndown;

import hu.adsd.dashboard.employee.EmployeeData;
import hu.adsd.dashboard.employee.EmployeeDataRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BurndownController {

    private final BurndownDataRepository burndownDataRepository;
    private final EmployeeDataRepository employeeDataRepository;

    // Spring Boot will automatically inject these repositories at initialisation
    public BurndownController(
            BurndownDataRepository burndownDataRepository,
            EmployeeDataRepository employeeDataRepository) {

        this.burndownDataRepository = burndownDataRepository;
        this.employeeDataRepository = employeeDataRepository;
    }

    @GetMapping("/burndowndata")
    public List<BurndownData> getBurndownData() {

        LocalDate date1 = LocalDate.parse("2020-11-30");
        LocalDate date2 = LocalDate.parse("2020-12-11");

        return burndownDataRepository.findAllByDateBetweenOrderByDate(date1, date2);
    }

    @GetMapping("/generateestimatedburndowndata")
    public void generateEstimatedBurndownData() {

        // Temporarily clear repository to prevent duplicate date
        burndownDataRepository.deleteAll();

        // Will be passed as parameters from API
        String startDateString = "2020-11-30";
        String endDateString = "2020-12-11";
        double totalStoryPointsPerSprint = 150;

        // Method variables
        //
        // All Developers
        List<EmployeeData> allDevelopers = employeeDataRepository.findAllByIsDeveloperTrue();
        //
        // The total hours and estimated SP per day
        double[] totalWorkingHoursPerDay = new double[7];
        double[] estimatedStoryPointsPerDayArray = new double[7];
        //
        // The total hours in the entire sprint
        int totalWorkingHoursPerSprint = 0;
        //
        // The burndownDataPoints List
        List<BurndownData> burndownDataPoints = new ArrayList<>();

        // Loop over Employees to get total working hours per sprint and per day
        for ( EmployeeData developer : allDevelopers)
        {
            // Get total hours per day
            totalWorkingHoursPerDay[0] += developer.getWorkingHoursMo();
            totalWorkingHoursPerDay[1] += developer.getWorkingHoursTu();
            totalWorkingHoursPerDay[2] += developer.getWorkingHoursWe();
            totalWorkingHoursPerDay[3] += developer.getWorkingHoursTh();
            totalWorkingHoursPerDay[4] += developer.getWorkingHoursFr();
        }

        for ( LocalDate sprintDay : new SprintIterator( startDateString, endDateString ))
        {
            int dayIndex = sprintDay.getDayOfWeek().getValue() - 1;
            totalWorkingHoursPerSprint += totalWorkingHoursPerDay[dayIndex];
        }

        // Calculate SP per day
        for ( int i = 0; i < totalWorkingHoursPerDay.length; i++ )
        {
            estimatedStoryPointsPerDayArray[ i ] =
                    ( totalWorkingHoursPerDay[ i ] / totalWorkingHoursPerSprint ) * totalStoryPointsPerSprint;
        }

        // Loop over days in sprint
        for ( LocalDate sprintDay : new SprintIterator( startDateString, endDateString ))
        {
            // Get the day index
            int dayIndex = sprintDay.getDayOfWeek().getValue() - 1;

            // Get the estimated SP from current day
            totalStoryPointsPerSprint -= estimatedStoryPointsPerDayArray[ dayIndex ];

            // Create burndownData point and add it to ArrayList
            BurndownData burndownData = new BurndownData( sprintDay, ( int ) totalStoryPointsPerSprint );
            burndownDataPoints.add(burndownData);
        }

        // Save all burndownData points to repository
        burndownDataRepository.saveAll(burndownDataPoints);
    }
}