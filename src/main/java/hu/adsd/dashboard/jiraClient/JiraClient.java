package hu.adsd.dashboard.jiraClient;

import hu.adsd.dashboard.issue.Issue;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    //https://andgreg.atlassian.net/rest/api/2/search?
    // search for all issues  of all the projects is Jira

    public static List<Issue> getAllIssues()
    {

        String query="https://andgreg.atlassian.net/rest/api/2/search?";
        List<Issue> issueList =new ArrayList<Issue> ();

        kong.unirest.HttpResponse<JsonNode> response = Unirest.get(query)
                // email and token of Jira account
                .basicAuth("alenasavachenko3@gmail.com", "XaSvh24eI7ftgqS8gV0q978A")
                .header("Accept", "application/json")
                .asJson();

        // response parse data

       JSONObject resObj=response.getBody().getObject();
       JSONArray issues=resObj.getJSONArray("issues");

        //System.out.println(" how much issues? "+ issues.length());


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

           // check if description not Null Object

           if ( fieldsObject.isNull("description"))
           {

               issue.setDescription("");
               issueList.add(issue);

           }
           else
           {
               String desc=fieldsObject.getString("description");
               //System.out.println("item dscription:"+desc);
               issue.setDescription(desc);
                // cut number Story points from description, search for number story points with regular expresion

               Pattern pattern =Pattern.compile("SP-(\\d*)",Pattern.CASE_INSENSITIVE);
               Matcher matcher=pattern.matcher(desc);

               if(matcher.matches())
               {
                   // cut "Sp-" away, save story points as integer
                   int storyPoints=Integer.parseInt(matcher.group().substring(3));
                   issue.setStoryPoints(storyPoints);

               }

               //assignee, team member who works with user story

               //check if not null
               if ( fieldsObject.isNull("assignee"))
               {

               }

               else
               {
                   String assigne= fieldsObject.getJSONObject("assignee").getString("displayName");
                   issue.setAssignedTo(assigne);

               }


              //add issue by issue in a list with all issues

              issueList.add(issue);


           }
       }

       // System.out.println( "size issue list: "+ issueList.size());
        return issueList;
    }


}