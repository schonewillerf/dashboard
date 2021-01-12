launchModal();

function voteSentiment(vote){
    httpGetAsync('/sentimentdata/' + vote, 'POST', function (){
        getSentimentData();
        document.getElementById("vote-block").classList.remove("d-flex");
        document.getElementById("vote-block").classList.add("d-none");
        document.getElementById("sentiment-chart").classList.remove("d-none");

        // Refresh burdown
        updateBurndownSentiment();
    });
}

//launch modal by applictaion start
function launchModal()
{
    $(window).on('load',function(){
        $('#voteModal').modal('show');

    });
}
function confetti(){
    party.screen({
        count: 500 * (window.innerWidth / 1980),
        countVariation: 0.5,
        angleSpan: 0,
        yVelocity: -100,
        yVelocityVariation: 2,
        rotationVelocityLimit: 6,
        scaleVariation: 0.8
    });
}