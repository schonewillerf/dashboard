
//subscribe server sent events
var source =new EventSource("/update");

source.addEventListener("updates", function(event)
{
    switch (event.data){
        case "updateSentiment" :
            console.log("Update the sentiment");
            updateSentimentChart();
            updateBurndownSentiment();
            break;
        case "updateScrumboard" :
            console.log("Update the scrumboard");
            updateBurndown();
            updateUpdates();
            getProjectSummary();
            break;
        default:
            console.log("Something went wrong");
            break;
    }
});

