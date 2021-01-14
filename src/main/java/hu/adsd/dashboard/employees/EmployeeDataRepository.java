package hu.adsd.dashboard.employees;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeDataRepository extends JpaRepository<EmployeeData, Integer>
{
    List<EmployeeData> findAllByIsDeveloperTrue();
}
