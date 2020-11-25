package hu.adsd.dashboard.burndown;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BurndownDataRepository extends JpaRepository<BurndownData, Integer> {
    List<BurndownData> findAllByDateBetweenOrderByDate(Date date, Date date2);
}
