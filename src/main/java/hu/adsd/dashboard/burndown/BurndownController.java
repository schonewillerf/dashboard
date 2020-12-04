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
        LocalDate startDate = LocalDate.parse("2020-11-30");
        LocalDate endDate = LocalDate.parse("2020-12-11");
        double totalStoryPointsPerSprint = 150;

        // Method variables
        List<EmployeeData> allEmployees = employeeDataRepository.findAll();
        int totalWorkingHoursPerSprint = 0;
        int totalWorkingHoursMonday = 0;
        int totalWorkingHoursTuesday = 0;
        int totalWorkingHoursWednesday = 0;
        int totalWorkingHoursThursday = 0;
        int totalWorkingHoursFriday = 0;

        // Loop over Employees to get total working hours per sprint and per day
        for (EmployeeData employeeData : allEmployees) {
            if (employeeData.isDeveloper()) {
                // Get total hours per sprint
                totalWorkingHoursPerSprint += employeeData.getTotalWorkingHours();
                // Get total hours per day
                totalWorkingHoursMonday += employeeData.getWorkingHoursMo();
                totalWorkingHoursTuesday += employeeData.getWorkingHoursTu();
                totalWorkingHoursWednesday += employeeData.getWorkingHoursWe();
                totalWorkingHoursThursday += employeeData.getWorkingHoursTh();
                totalWorkingHoursFriday += employeeData.getWorkingHoursFr();
            }
        }

        // Calculate estimated storypoints that can be delivered per day based on working hours
        double estimatedStoryPointsMonday = ((double) totalWorkingHoursMonday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;
        double estimatedStoryPointsTuesday = ((double) totalWorkingHoursTuesday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;
        double estimatedStoryPointsWednesday = ((double) totalWorkingHoursWednesday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;
        double estimatedStoryPointsThursday = ((double) totalWorkingHoursThursday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;
        double estimatedStoryPointsFriday = ((double) totalWorkingHoursFriday / totalWorkingHoursPerSprint) * totalStoryPointsPerSprint;

        // Prepare weekly array of estimated storypoint
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

        // Loop over days in sprint
        while (startDate.compareTo(endDate) <= 0) {

            // Get the estimated storypoints from current day
            int dayOfWeek = startDate.getDayOfWeek().getValue() - 1;
            totalStoryPointsPerSprint -= estimatedStoryPointsPerDayArray[dayOfWeek];

            // Extra check to make sure we don't end with negative numbers
            // possibly due to some double to integer conversion
            if (startDate.compareTo(endDate) == 0) {
                totalStoryPointsPerSprint = 0;
            }

            // Create burndowData point and add it to ArrayList
            BurndownData burndownData = new BurndownData(startDate, (int) totalStoryPointsPerSprint);
            burndownDataPoints.add(burndownData);

            // Increment the day in the loop
            startDate = startDate.plusDays(1);
        }

        // Save all burndownData points to repository
        burndownDataRepository.saveAll(burndownDataPoints);
    }
}