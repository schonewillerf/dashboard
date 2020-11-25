function voteSentiment(vote){
    //Send HTTP request
    const Http = new XMLHttpRequest();
    const url = 'http://localhost:8080/sentimentdata/' + vote;
    Http.open("POST", url);
    Http.send();

    Http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            getSentimentData();
            document.getElementById("vote-block").classList.remove("d-flex");
            document.getElementById("vote-block").classList.add("d-none");
            document.getElementById("sentiment-chart").classList.remove("d-none");
        }
    }
}