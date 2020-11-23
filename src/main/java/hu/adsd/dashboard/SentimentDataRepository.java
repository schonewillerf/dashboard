package hu.adsd.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface SentimentDataRepository extends JpaRepository<SentimentData, Integer> {
    @Query("SELECT value, COUNT(id) FROM SentimentData WHERE date = ?1 GROUP BY value ORDER BY value ASC")
    List<Object[]> countTotalCommentsByYear(Date date);
}
