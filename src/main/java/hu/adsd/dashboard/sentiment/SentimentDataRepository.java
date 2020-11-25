package hu.adsd.dashboard.sentiment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface SentimentDataRepository extends JpaRepository<SentimentData, Integer> {

    @Query("SELECT SDE.description, COUNT(SD.id) FROM SentimentData as SD JOIN SentimentDescription as SDE ON SDE.id = SD.value WHERE SD.date = ?1 GROUP BY SD.value")
    List<Object[]> countSentimentByValue(Date date);
}
