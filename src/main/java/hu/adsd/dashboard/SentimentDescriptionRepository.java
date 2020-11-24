package hu.adsd.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SentimentDescriptionRepository extends JpaRepository<SentimentDescription, Integer> {
    List<SentimentDescription> getAllById(int id);
}
