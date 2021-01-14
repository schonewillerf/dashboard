package hu.adsd.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import hu.adsd.dashboard.burndown.SprintIterator;
import hu.adsd.dashboard.employees.EmployeeData;

@SpringBootTest
class DashboardApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testEmployeeName() {
		EmployeeData employee = new EmployeeData("Raymen");
		assertEquals("Raymen", employee.getName());
	}

	@Test
	void testSprintIteratorDuration() {

		int days = 0;
		
		for(LocalDate date : new SprintIterator("2021-01-01", "2021-01-05")) {
			days += 1;
		}

		assertEquals(5, days);
	}

}
