package hu.adsd.dashboard.topContributors;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopContributorDataRepository extends JpaRepository<TopContributorData, Integer> {
    List<TopContributorData> findTop5ByOrderByStoryPointsDesc();
}
