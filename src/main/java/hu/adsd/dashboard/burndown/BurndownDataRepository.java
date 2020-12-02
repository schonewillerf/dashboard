package hu.adsd.dashboard.burndown;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BurndownDataRepository extends JpaRepository<BurndownData, Integer> {
    List<BurndownData> findAllByDateBetweenOrderByDate(LocalDate date, LocalDate date2);
}
