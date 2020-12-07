package hu.adsd.dashboard.issue;

import hu.adsd.dashboard.burndown.BurndownData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Integer>
{

    List<Issue> findByIssueStatusIgnoreCase(String issueStatus);

    public Issue findOneByIssueKeyIgnoreCase(String key);

}



