package hu.adsd.dashboard.jiraClient;

import hu.adsd.dashboard.issue.Issue;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JiraClient {


    public static JSONObject sendNetworkRequest(String query)
    {
      Authentification authentification=new Authentification();
      kong.unirest.HttpResponse<JsonNode> response = Unirest.get(query)
                .basicAuth(authentification.getUserEmail(), authentification.getToken())
                .header("Accept", "application/json")
                .asJson();

        JSONObject responseObject=response.getBody().getObject();
        return responseObject;

    }



    public  int getStatistics (String projectName, String taskName )
    {
        // get statistics by project name and task status

        String filter="status=%22"+taskName+"%22%20AND%20project=%22"+projectName+"%22";
        String query="https://andgreg.atlassian.net/rest/api/2/search?jql="+filter;

        JSONObject resObj = sendNetworkRequest(query);
        int total = resObj.getInt("total");
        return total;


    }



    public static List<Issue> getAllIssues()
    {
        // search for all issues  of all the projects is Jira
        String query="https://andgreg.atlassian.net/rest/api/2/search?";
        JSONObject resObj = sendNetworkRequest(query);
        JSONArray issues=resObj.getJSONArray("issues");
        List<Issue> parsedIssueList = parseIssues(issues);
        return parsedIssueList;

    }


    public static String[] getkeysOfRecentUpdatedIssues(String withinPeriodOfTime, int maxResult)
    {
        String query="https://andgreg.atlassian.net/rest/api/2/search?jql=updated%20%3E=%20-"+withinPeriodOfTime+"%20order%20by%20updated%20DESC&maxResults="+maxResult;
        JSONObject responseObject=sendNetworkRequest(query);
        JSONArray issues=responseObject.getJSONArray("issues");
        int size=issues.length();
        String[] keyList=new String[size];

        for (int i=0;i<size; i++)
        {
            JSONObject jsonObjectIssues=issues.getJSONObject(i);
            String issueKey=jsonObjectIssues.getString("key");

            keyList[i]=issueKey;
        }

        return keyList;
    }



    public static List<Issue> parseIssues(JSONArray issues)
    {

        List<Issue> issueList =new ArrayList<Issue> ();
        for(int i=0; i<issues.length();i++)
        {
            Issue issue=new Issue();
            // standard  parsing of json response to retrieve important information
            JSONObject jsonObjectIssue=issues.getJSONObject(i);
            String issueKey=jsonObjectIssue.getString("key");
            issue.setIssueKey(issueKey);
            JSONObject fieldsObject =jsonObjectIssue.getJSONObject("fields");
            String statusCategoryChangeDate= fieldsObject.getString("statuscategorychangedate");
            issue.setStatusCatogryChangedData(statusCategoryChangeDate);
            String projectKey=fieldsObject.getJSONObject("project").getString("key");
            issue.setProjectKey(projectKey);
            String projectName=fieldsObject.getJSONObject("project").getString("name");
            issue.setProjectName(projectName);
            String status =fieldsObject.getJSONObject("status").getString("name");
            issue.setIssueStatus(status);

            if (notNullObject(fieldsObject,"summary"))
            {
                String issueName=fieldsObject.getString("summary");
                issue.setIssueName(issueName);
            }

            if (notNullObject(fieldsObject,"assignee"))
            {
                String assigne= fieldsObject.getJSONObject("assignee").getString("displayName");
                issue.setAssignedTo(assigne);
            }

            if(notNullObject(fieldsObject,"description"))
            {
                String description=fieldsObject.getString("description");
                issue.setDescription(description);
                issue.setStoryPoints(findSPwithRegex(description));
            }

            //add issue by issue in a list with all issues
                issueList.add(issue);

            }

        return issueList;

    }



    private static boolean notNullObject(JSONObject obj, String key)
    {
        if ( !(obj.isNull(key)))
        return true;

        else
        return false;

    }

    public static int findSPwithRegex(String description)
    {
        Pattern pattern =Pattern.compile("SP-(\\d*)",Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(description);
        if(matcher.find())
        {                    // cut "Sp-" away, save story points as integer
            int storyPoints=Integer.parseInt(matcher.group().substring(3));

            return storyPoints;

            }

        return 0;
    }



}
