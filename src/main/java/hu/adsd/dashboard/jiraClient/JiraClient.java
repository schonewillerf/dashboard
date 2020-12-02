package hu.adsd.dashboard.jiraClient;

import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JiraClient {


    public static int getStatistics (String projectName, String taskName )
    {
        // to gerenate filter you need have project Name and task name

        String filter="status=%22"+taskName+"%22%20AND%20project=%22"+projectName+"%22";
        String query="https://andgreg.atlassian.net/rest/api/2/search?jql="+filter;
        // use  Unirest library to connect to Api, you need to add unirest dependency to your pom.xml

        kong.unirest.HttpResponse<JsonNode> response = Unirest.get(query)
                // email and token of Jira account
                .basicAuth("alenasavachenko3@gmail.com", "XaSvh24eI7ftgqS8gV0q978A")
                .header("Accept", "application/json")
                .asJson();

        //parse response
        JSONObject resObj = response.getBody().getObject();
        int total = resObj.getInt("total");
        //System.out.println("totaal task  : "+total);

        return total;


    }


}
