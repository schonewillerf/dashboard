// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

// Local variable for burndownChart
var burndownChart;

// Update method
function updateBurndownSentiment() {
    const labels = burndownChart.data.labels;

    httpGetAsync('http://localhost:8080/sentimentdailychart', 'GET', function (result){
        const json = JSON.parse(result);

        // Update Sentiment Dataset
        burndownChart.data.datasets[2].data = parseSentimentData(labels, json);
        burndownChart.update();
    });
}

httpGetAsync('http://localhost:8080/burndowndata', 'GET', function (result){
    // Variables
    const labels = [];
    const currentQuantityArray = [];
    const estimatedQuantityArray = [];
    let sentimentScoreArray;

    // Parse JSON
    const json = JSON.parse(result);
    console.log(result);

    // Loop over Indicator Objects
    for (let i = 0; i < json[0].length; i++) {
        const obj = json[0][i];

        const formattedDate = new Date(obj.date);
        labels.push(formattedDate.getDate() + "-" + (formattedDate.getMonth() + 1) + "-" + formattedDate.getFullYear());

        const currentQuantity = obj.currentQuantity;
        const estimatedQuantity = obj.estimatedQuantity;

        if (currentQuantity !== -1) {
            currentQuantityArray.push(currentQuantity);
        }

        estimatedQuantityArray.push(estimatedQuantity);
    }

    sentimentScoreArray = parseSentimentData(labels, json[1]);

    // Area Chart Example
    let ctx = document.getElementById("myAreaChart");
    burndownChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                yAxisID: 'A',
                label: "Actueel",
                lineTension: 0,
                backgroundColor: "rgba(78, 115, 223, 0.05)",
                borderColor: "rgba(78, 115, 223, 1)",
                pointRadius: 3,
                pointBackgroundColor: "rgba(78, 115, 223, 1)",
                pointBorderColor: "rgba(78, 115, 223, 1)",
                pointHoverRadius: 3,
                pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                pointHitRadius: 10,
                pointBorderWidth: 2,
                data: currentQuantityArray,
            },
                {
                    yAxisID: 'A',
                    label: "Ideaal",
                    lineTension: 0,
                    backgroundColor: "rgba(28, 200, 138, 0.05)",
                    borderColor: "rgba(28, 200, 138, 1)",
                    pointRadius: 3,
                    pointBackgroundColor: "rgba(28, 200, 138, 1)",
                    pointBorderColor: "rgba(28, 200, 138, 1)",
                    pointHoverRadius: 3,
                    pointHoverBackgroundColor: "rgba(28, 200, 138, 1)",
                    pointHoverBorderColor: "rgba(28, 200, 138, 1)",
                    pointHitRadius: 10,
                    pointBorderWidth: 2,
                    data: estimatedQuantityArray,
                },
                {
                    yAxisID: 'B',
                    label: "Sentiment",
                    lineTension: 0.3,
                    backgroundColor: "rgba(246, 194, 62, 0.05)",
                    borderColor: "rgba(246, 194, 62, 1)",
                    pointRadius: 3,
                    pointBackgroundColor: "rgba(246, 194, 62, 1)",
                    pointBorderColor: "rgba(246, 194, 62, 1)",
                    pointHoverRadius: 3,
                    pointHoverBackgroundColor: "rgba(246, 194, 62, 1)",
                    pointHoverBorderColor: "rgba(246, 194, 62, 1)",
                    pointHitRadius: 10,
                    pointBorderWidth: 2,
                    data: sentimentScoreArray,
                }],
        },
        options: {
            spanGaps: true,
            maintainAspectRatio: false,
            scales: {
                xAxes: [{
                    time: {
                        unit: 'date'
                    },
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    ticks: {
                        maxTicksLimit: 7
                    }
                }],
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: 'Story Points',
                    },
                    id: 'A',
                    type: 'linear',
                    position: 'left',
                    ticks: {
                        maxTicksLimit: 5,
                        padding: 10
                    },
                    gridLines: {
                        color: "rgb(234, 236, 244)",
                        zeroLineColor: "rgb(234, 236, 244)",
                        drawBorder: false,
                        borderDash: [2],
                        zeroLineBorderDash: [2]
                    }
                },
                    {
                        scaleLabel: {
                            display: true,
                            labelString: 'Sentiment',
                        },
                        id: 'B',
                        type: 'linear',
                        position: 'right',
                        ticks: {
                            // Change values to smileys
                            callback: function(value, index, values) {
                                if (value === 1) {
                                    return "😕";
                                }
                                if (value === 2) {
                                    return "🙂";
                                }
                                if (value === 3) {
                                    return "😁";
                                }
                                return "";
                            },
                            steps: 2,
                            stepValue: 1,
                            min: 1,
                            max: 3,
                            beginAtZero: false,
                            maxTicksLimit: 5,
                            padding: 10,
                        },
                        gridLines: {
                            display: false,
                        }
                    }
                ],
            },
            legend: {
                display: true
            },
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                titleMarginBottom: 10,
                titleFontColor: '#6e707e',
                titleFontSize: 14,
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                intersect: false,
                mode: 'index',
                caretPadding: 10,
                callbacks: {
                    label: function (tooltipItem, chart) {
                        var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                        return datasetLabel + " " + tooltipItem.yLabel;
                    }
                }
            }
        }
    });
});

function parseSentimentData(labels, sentimentData) {
    //Loop through dates to see if there is sentiment data
    let sentimentScoreArray = [];

    for (let j = 0; j < labels.length; j++){
        const date = labels[j];

        let exists = 0;
        for (let k = 0; k < sentimentData.length; k++) {
            const jsonDate = new Date(sentimentData[k].date);
            const jsonFormattedDate = jsonDate.getDate() + "-" + (jsonDate.getMonth() + 1) + "-" + jsonDate.getFullYear();

            if (jsonFormattedDate === date){
                exists = 1;
                sentimentScoreArray.push(sentimentData[k].averageSentiment);
                break;
            }
        }
        if(exists === 0){
            sentimentScoreArray.push(null);
        }
    }

    return sentimentScoreArray;
}


