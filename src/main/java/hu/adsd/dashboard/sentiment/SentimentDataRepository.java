package hu.adsd.dashboard.sentiment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SentimentDataRepository extends JpaRepository<SentimentData, Integer> {
    // Method with JPQL query
    @Query("SELECT SDE.description, COUNT(SD.id) FROM SentimentData as SD " +
            "JOIN SentimentDescription as SDE ON SDE.id = SD.value " +
            "WHERE SD.date = ?1 " +
            "GROUP BY SD.value")
    List<Object[]> countSentimentByValue( LocalDate date);

    @Query("SELECT AVG(value) FROM SentimentData WHERE date = ?1")
    double calculateAverageSentiment(LocalDate date);
}
