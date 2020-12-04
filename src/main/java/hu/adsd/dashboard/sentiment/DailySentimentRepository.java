package hu.adsd.dashboard.sentiment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailySentimentRepository extends JpaRepository<DailySentiment, Integer>
{
    Optional<DailySentiment> findByDate( LocalDate date );
}
