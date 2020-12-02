// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

//Send HTTP request
const Http = new XMLHttpRequest();
const url = 'http://localhost:8080/burndowndata';
Http.open("GET", url);
Http.send();

//Wait for HTTP response
Http.onreadystatechange = function () {

    // Typical action to be performed when the document is ready:
    if (this.readyState == 4 && this.status == 200) {

        // Variables
        var labels = new Array();
        var currentQuantityArray = new Array();
        var estimatedQuantityArray = new Array();
        var sentimentScoreArray = [];

        // Parse JSON
        var json = JSON.parse(Http.responseText);

        // Loop over Indicator Objects
        for (var i = 0; i < json.length; i++) {
            var obj = json[i];

            var formattedDate = new Date(obj.date);
            labels.push(formattedDate.getDate() + "-" + formattedDate.getMonth() + "-" + formattedDate.getFullYear());

            var currentQuantity = obj.currentQuantity;
            var estimatedQuantity = obj.estimatedQuantity;
            var sentimentScore = obj.sentimentScore;

            if (currentQuantity !== -1) {
                currentQuantityArray.push(currentQuantity);
            }

            // Check if the sentimentScore exists
            if (sentimentScore !== -1) { sentimentScoreArray.push(sentimentScore); }

            estimatedQuantityArray.push(estimatedQuantity);
        }

        // Area Chart Example
        var ctx = document.getElementById("myAreaChart");
        var myLineChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    yAxisID: 'A',
                    label: "Actueel",
                    lineTension: 0.3,
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
                    lineTension: 0.3,
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
                maintainAspectRatio: false,
                layout: {
                    padding: {
                        left: 10,
                        right: 25,
                        top: 25,
                        bottom: 0
                    }
                },
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
                            // Include a dollar sign in the ticks
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
                    display: false
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
    }
};

