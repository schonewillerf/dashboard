function getSentimentData() {
//Send HTTP request
    const Http2 = new XMLHttpRequest();
    const url2 = 'http://localhost:8080/sentimentdata';
    Http2.open("GET", url2);
    Http2.send();

    Http2.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            var json = JSON.parse(Http2.responseText);
            var values = new Array();
            var names = new Array();

            for (var i = 0; i < json.length; i++) {
                var obj = json[i];
                values.push(obj[1]);
                names.push(obj[0]);
            }
            displaySentimentChart(values, names);
        }
    };
}

function getProjectSummary(){
    //Send HTTP request
    const Http = new XMLHttpRequest();
    const url = 'http://localhost:8080/projectsummary';
    Http.open("GET", url);
    Http.send();

    Http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            var json = JSON.parse(Http.responseText);
            var html = "";
            var colors = ["#e74a3b", "#fd7e14", "#f6c23e", "#1cc88a"];

            for (var i = 0; i < json.length; i++) {
                var obj = json[i];
                html += "<div class='col-6 mt-2' style='color: " + colors[i] + "'>" + obj.name + "</div>" +
                    "<div class='col-3 mt-2' style='color: " + colors[i] + "'>" + obj.items + "</div>" +
                    "<div class='col-3 mt-2' style='color: " + colors[i] + "'>" + obj.storyPoints + "</div>";
            }
            document.getElementById("project-summary-data").innerHTML = html;
            summaryChart(json);
        }
    };
}
getProjectSummary();