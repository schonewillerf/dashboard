package hu.adsd.dashboard.jiraClient;

import hu.adsd.dashboard.issue.Issue;
import hu.adsd.dashboard.issue.UpdatedItem;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



    public static List<Issue> getAllIssues()
    {
        // search for all issues  of all the projects is Jira
        String query="https://andgreg.atlassian.net/rest/api/2/search?";
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
            //get  histpry of last change


            JSONArray history=updatedItemObject.getJSONObject("changelog").getJSONArray("histories");


            for (int j=0; j<history.length();j++)
            {
                JSONObject lastHistory= (JSONObject) history.get(j);
                JSONArray changeLogArray= lastHistory.getJSONArray("items");

                for ( int n=0; n<changeLogArray.length(); n++)
                {
                    JSONObject logItemObject = changeLogArray.getJSONObject(n);
                    String changedStatus =logItemObject.getString("field");
                    if(!changedStatus.equalsIgnoreCase("rank"))
                    {
                        switch(changedStatus)
                        {
                            case "status":
                                System.out.println("status: "+changedStatus);
                                String fromStatus=logItemObject.getString("fromString");
                                update.setChangedStatusFrom(fromStatus);
                                String toStatus=logItemObject.getString("toString");
                                update.setChangedStatusTo(toStatus);
                                System.out.println("status to "+ toStatus+ " key us"+ update.getItemKey());
                                if (toStatus.equals("Done"))
                                {
                                    update.setResolved(true);

                                }
                                break;
                            case "description":
                                System.out.println("description"+changedStatus);
                                break;
                            case "resolution":

                                System.out.println("resolution: "+changedStatus);
                                break;
                            case "Story point estimate":
                                System.out.println("sp"+changedStatus);
                                break;

                            default:

                                System.out.println("no status"+changedStatus);
                        }

                    }

                }


            }

/*
            JSONObject lastHistory= (JSONObject) history.get(0);
            String datumStringUpdate=lastHistory.getString("created");
           // System.out.println("datum update: "+datumStringUpdate);
            LocalDate date=LocalDate.parse(datumStringUpdate.substring(0,10));
            update.setLastChangedOn(date);
            //System.out.println("loca date: "+date);
            // loop through change log
            JSONArray changeLogArray= lastHistory.getJSONArray("items");
            for ( int n=0; n<changeLogArray.length(); n++)
            {
                JSONObject logItemObject = changeLogArray.getJSONObject(n);
                String changedStatus =logItemObject.getString("field");
                //System.out.println("changed status : "+ changedStatus);
                if(changedStatus.equalsIgnoreCase("status"))
                {
                    String fromStatus=logItemObject.getString("fromString");
                    update.setChangedStatusFrom(fromStatus);
                    String toStatus=logItemObject.getString("toString");
                    update.setChangedStatusTo(toStatus);
                    System.out.println("status to "+ toStatus+ " key us"+ update.getItemKey());
                    if (toStatus.equals("Done"))
                    {
                        update.setResolved(true);
                    }
                }

                if (notNullObject(fieldsObject,"summary"))
                {
                    String itemSummary=fieldsObject.getString("summary");
                    update.setItemSummary(itemSummary);
                }


                JSONObject histoiresAuthor=lastHistory.getJSONObject("author");
                JSONObject avatarsUrl= (JSONObject) histoiresAuthor.get("avatarUrls");
                String avatorUrl=avatarsUrl.getString("48x48");

                String displayName=histoiresAuthor.getString("displayName");

                update.setAuthor(displayName);
                update.setAvatarUrl(avatorUrl);
            }

            String status =fieldsObject.getJSONObject("status").getString("name");
            update.setItemStatus(status);
*/

            list.add(update);


        }

        System.out.println("list length? "+list.size());

        return list;
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
