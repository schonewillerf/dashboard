package hu.adsd.dashboard.topContributors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopContributorController {
    // Properties
    private final TopContributorDataRepository topContributorDataRepository;

    // Constructor
    public TopContributorController(TopContributorDataRepository topContributorDataRepository) {
        this.topContributorDataRepository = topContributorDataRepository;
    }

    @GetMapping("/topcontributordata")
    public List<TopContributorData> getTopContributorData() {
        return topContributorDataRepository.findTop5ByOrderByStoryPointsDesc();
    }
}
