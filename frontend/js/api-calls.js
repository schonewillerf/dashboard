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

            for (var i = 0; i < json.length; i++) {
                var obj = json[i];
                html += "<tr>" +
                    "<td>" + obj.name + "</td>" +
                    "<td>" + obj.value + "</td>" +
                    "</tr>";
            }
            document.getElementById("project-summary-table").innerHTML = html;
        }
    };
}
getProjectSummary();