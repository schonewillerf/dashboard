package hu.adsd.dashboard.employee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDataRepository extends JpaRepository<EmployeeData, Integer> {
}
