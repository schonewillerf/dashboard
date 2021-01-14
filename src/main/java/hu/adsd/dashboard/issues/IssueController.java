package hu.adsd.dashboard.issues;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IssueController {

    // Properties
    private final UpdateItemRepository updateItemRepository;

    // Constructor
    public IssueController(UpdateItemRepository updateItemRepository) {
        this.updateItemRepository = updateItemRepository;
    }

    @RequestMapping("/getUpdatedTasks")
    public List<UpdatedItem> getUpdatedTasks() {
        return updateItemRepository.findAll();
    }
}
