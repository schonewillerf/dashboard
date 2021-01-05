//Asynchronous HTTP call
function httpGetAsync(theUrl, method, callback)
{
    var xmlHttp = new XMLHttpRequest();
    //Check if state has changed
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open(method, theUrl, true); // true for asynchronous
    xmlHttp.send(null);
}

function getSentimentData() {
    httpGetAsync('/sentimentdata', 'GET', function (result){
        const json = JSON.parse(result);
        const values = [];
        const names = [];

        for (let i = 0; i < json.length; i++) {
            const obj = json[i];
            values.push(obj[1]);
            names.push(obj[0]);
        }
        displaySentimentChart(values, names);
    })
}

function getProjectSummary(){
    httpGetAsync('/projectsummary', 'GET', function(result){
        let json = JSON.parse(result);
        let html = "";
        const colors = ["#e74a3b", "#fd7e14", "#f6c23e", "#1cc88a"];

        for (let i = 0; i < json.length; i++) {
            const obj = json[i];
            html += "<div class='col-6 mt-2' style='color: " + colors[i] + "'>" + obj.name + "</div>" +
                "<div class='col-3 mt-2' style='color: " + colors[i] + "'>" + obj.items + "</div>" +
                "<div class='col-3 mt-2' style='color: " + colors[i] + "'>" + obj.storyPoints + "</div>";
        }
        document.getElementById("project-summary-data").innerHTML = html;
        summaryChart(json);
    });
}
getProjectSummary();