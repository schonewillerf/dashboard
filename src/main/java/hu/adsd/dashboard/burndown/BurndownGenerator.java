package hu.adsd.dashboard.burndown;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.adsd.dashboard.employee.EmployeeData;
import hu.adsd.dashboard.employee.EmployeeDataRepository;

@Component
public class BurndownGenerator {

    private final BurndownDataRepository burndownDataRepository;
    private final EmployeeDataRepository employeeDataRepository;

    public BurndownGenerator(
        BurndownDataRepository burndownDataRepository,
        EmployeeDataRepository employeeDataRepository
    ) {
        this.burndownDataRepository = burndownDataRepository;
        this.employeeDataRepository = employeeDataRepository;
    }

    public void generateEstimatedBurndownData(Sprint sprint) {

        // Temporarily clear repository to prevent duplicate date
        burndownDataRepository.deleteAll();

        // Will be passed as parameters from API
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

        // Calculate total hours in sprint
        for ( LocalDate sprintDay : new SprintIterator( sprint.getStartDateString(), sprint.getEndDateString() ))
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
        for ( LocalDate sprintDay : new SprintIterator( sprint.getStartDateString(), sprint.getEndDateString() ))
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
