const webSocket = new WebSocket('ws://localhost:8080/websocket');

webSocket.onerror = function(event) {
    onError(event)
};

webSocket.onopen = function(event) {
    onOpen(event)
};

webSocket.onmessage = function(event) {
    onMessage(event)
};

function onMessage(event) {
    switch (event.data){
        case "updateBurndown":
            console.log("Update the burndown");
            updateBurndown();
            break;
        case "updateSentiment" :
            console.log("Update the sentiment");
            updateSentimentChart();
            updateBurndownSentiment();
            break;
        default:
            console.log("Something went wrong");
            break;
    }
}

function onOpen(event) {
    console.log('Connection established');
}

function onError(event) {
    console.log("Error: " + event.data);
}