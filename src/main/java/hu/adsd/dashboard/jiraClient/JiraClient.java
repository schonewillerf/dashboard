package hu.adsd.dashboard.jiraClient;

import hu.adsd.dashboard.burndown.Sprint;
import hu.adsd.dashboard.issues.Issue;
import hu.adsd.dashboard.issues.UpdatedItem;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class JiraClient {

    public static void main(String[] args) {

        getUpdates("1w", 7);

    }

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

    public static Sprint getCurrentSprint() {

        String query = "https://andgreg.atlassian.net/rest/agile/1.0/board/3/sprint?state=active";
        JSONObject resObj = sendNetworkRequest(query);
        JSONObject sprintArray = resObj.getJSONArray("values").getJSONObject(0);

        String startDate = sprintArray.getString("startDate").substring(0, 10);
        String endDate = sprintArray.getString("endDate").substring(0, 10);
        int jiraId = sprintArray.getInt("id");

        return new Sprint(startDate, endDate, jiraId);
    }



    public static List<Issue> getAllIssues()
    {
        // search for all issues  of all the projects is Jira
        String query="https://andgreg.atlassian.net/rest/api/2/search?jql=ORDER+BY+status+ASC";
        JSONObject resObj = sendNetworkRequest(query);
        JSONArray issues=resObj.getJSONArray("issues");
        List<Issue> parsedIssueList = parseIssues(issues);
        return parsedIssueList;

    }


    public static List<UpdatedItem>  getUpdates (String withinPeriodOfTime, int maxResult)
    {

        List<UpdatedItem> list=new ArrayList<>();
        String query="https://andgreg.atlassian.net/rest/api/2/search?jql=updated%20%3E=%20-"+withinPeriodOfTime+"%20order%20by%20updated%20DESC&maxResults="+maxResult+"&expand=changelog";
        JSONObject responseObject=sendNetworkRequest(query);
        JSONArray updatesItems=responseObject.getJSONArray("issues");
        for (int i=0; i<updatesItems.length(); i++)
        {
            UpdatedItem update=new UpdatedItem();
            JSONObject updatedItemObject=updatesItems.getJSONObject(i);
            String updatedKey=updatedItemObject.getString("key");
            update.setItemKey(updatedKey);
            JSONObject fieldsObject=updatedItemObject.getJSONObject("fields");
            //Stroy point are saved to customfield_10016
            if (notNullObject(fieldsObject,"customfield_10016"))
            {
                Integer sp=fieldsObject.getInt("customfield_10016");
                update.setStoryPoints(sp);

            }
            // user story status
            String status =fieldsObject.getJSONObject("status").getString("name");
            update.setItemStatus(status);
            // check if user story is resolved
            if(status.equalsIgnoreCase("done"))
            update.setResolved(true);
            // get user stories summary
            if (notNullObject(fieldsObject,"summary"))
            {
                String issueName=fieldsObject.getString("summary");
                update.setItemSummary(issueName);
            }

            // change logs
            JSONArray history=updatedItemObject.getJSONObject("changelog").getJSONArray("histories");
            loopThroughChangeLogs(history,update);
            list.add(update);


        }
        return list;
    }


    public static void loopThroughChangeLogs(JSONArray history, UpdatedItem update)
    {
        //outer loop
        for (int j=0; j<history.length();j++)
        {
            JSONObject lastHistory= (JSONObject) history.get(j);
            JSONArray changeLogArray= lastHistory.getJSONArray("items");
            JSONObject histoiresAuthor=lastHistory.getJSONObject("author");
            JSONObject avatarsUrl= (JSONObject) histoiresAuthor.get("avatarUrls");
            String avatorUrl=avatarsUrl.getString("32x32");
            String displayName=histoiresAuthor.getString("displayName");
            update.setAuthor(displayName);
            update.setAvatarUrl(avatorUrl);
            String datumStringUpdate=lastHistory.getString("created");
            LocalDate date=LocalDate.parse(datumStringUpdate.substring(0,10));
            update.setLastChangedOn(date);
            LocalTime time = LocalTime.parse(datumStringUpdate.substring(11,19));
            update.setLastChangedOnTime(time);
           for ( int n=0; n<changeLogArray.length(); n++)
            {
                JSONObject logItemObject = changeLogArray.getJSONObject(n);
                String changedStatus =logItemObject.getString("field");
                {
                    switch(changedStatus)
                    {
                       // if changed status = rank, than break inner loop and go to the outer loop
                        case "Rank":
                            break;
                        case "status":
                            update.setItemType("status");
                            String fromStatus=logItemObject.getString("fromString");
                            update.setChangedStatusFrom(fromStatus);
                            String toStatus=logItemObject.getString("toString");
                            update.setChangedStatusTo(toStatus);
                            // if changed status is found, than break inner & outer loop
                            return;
                        case "description":
                            update.setItemType("description");
                            // break inner & outer loop
                            return;
                        case "resolution":
                            update.setItemType("resolution");
                            // break inner loop and continue to outer loop
                            break;
                        case "Story point estimate":
                            update.setItemType("spEstimate");
                            // break inner & outer loop
                            return;
                          default:
                            System.out.println("no status "+changedStatus);
                    }
                }
            }
        }
    }

    public static List<Issue> parseIssues(JSONArray issues)
    {

        List<Issue> issueList =new ArrayList<Issue> ();
        for(int i=0; i<issues.length();i++)
        {
            Issue issue=new Issue();
            // standard  parsing of json response to retrieve important information
            JSONObject jsonObjectIssue=issues.getJSONObject(i);

            JSONObject fieldsObject =jsonObjectIssue.getJSONObject("fields");

            String status =fieldsObject.getJSONObject("status").getString("name");
            issue.setIssueStatus(status);

            //Stroy point are saved to customfield_10016
            if (notNullObject(fieldsObject,"customfield_10016"))
            {
                Integer sp=fieldsObject.getInt("customfield_10016");
                issue.setStoryPoints(sp);
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

}
