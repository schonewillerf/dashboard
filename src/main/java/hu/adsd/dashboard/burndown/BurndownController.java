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
        for (EmployeeData employeeData : allDevelopers)
        {
            // Get total hours per day
            totalWorkingHoursPerDay[0] += employeeData.getWorkingHoursMo();
            totalWorkingHoursPerDay[1] += employeeData.getWorkingHoursTu();
            totalWorkingHoursPerDay[2] += employeeData.getWorkingHoursWe();
            totalWorkingHoursPerDay[3] += employeeData.getWorkingHoursTh();
            totalWorkingHoursPerDay[4] += employeeData.getWorkingHoursFr();
        }

        // Needed an extra variable for this loop :(
        LocalDate loopDate = LocalDate.parse("2020-11-30");
        //
        // Loop over days in the sprint to calculate total developer hours
        while ( loopDate.compareTo( endDate ) <= 0 )
        {
            int dayOfWeek = loopDate.getDayOfWeek().getValue() - 1;

            totalWorkingHoursPerSprint += totalWorkingHoursPerDay[dayOfWeek];

            loopDate = loopDate.plusDays( 1 );
        }

        // Calculate SP per day
        for ( int i = 0; i < totalWorkingHoursPerDay.length; i++ )
        {
            if ( totalStoryPointsPerSprint > 0 )
            {
                estimatedStoryPointsPerDayArray[i] =
                        ( totalWorkingHoursPerDay[i] / totalWorkingHoursPerSprint ) * totalStoryPointsPerSprint;
            }
        }

        // Loop over days in sprint
        while (startDate.compareTo(endDate) <= 0) {

            // Get the estimated storypoints from current day
            int dayOfWeek = startDate.getDayOfWeek().getValue() - 1;
            totalStoryPointsPerSprint -= estimatedStoryPointsPerDayArray[dayOfWeek];

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