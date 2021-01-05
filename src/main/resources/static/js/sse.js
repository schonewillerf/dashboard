
//subscribe server sent events
var source =new EventSource("/subscribe");
source.addEventListener("webhookJira", function(event)
{
    console.log("recieved server sent event");
    location.reload();

});

