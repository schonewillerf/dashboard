// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

let myPieChart;

// Update method
function updateSentimentChart() {
    httpGetAsync('http://localhost:8080/sentimentdata', 'GET', function (result){
        const json = JSON.parse(result);
        const values = [];
        const names = [];

        for (let i = 0; i < json.length; i++) {
            const obj = json[i];
            values.push(obj[1]);
            names.push(obj[0]);
        }

        try {
            // Update Sentiment Dataset
            myPieChart.data.datasets[0].data = values;
            myPieChart.data.labels = names;
            myPieChart.update();
        }catch (error){}
    });
}

function displaySentimentChart(values, names) {
    // Pie Chart Example
    const ctx = document.getElementById("sentimentPieChart");
    myPieChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: names,
            datasets: [{
                data: values,
                backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],
                hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }],
        },
        options: {
            maintainAspectRatio: false,
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                caretPadding: 10,
            },
            legend: {
                display: false
            },
            cutoutPercentage: 80,
        },
    });
}
