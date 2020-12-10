package hu.adsd.dashboard.projectSummary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectSummaryDataRepository extends JpaRepository<ProjectSummaryData, Integer> {
       ProjectSummaryData findOneByName(String name);
}
