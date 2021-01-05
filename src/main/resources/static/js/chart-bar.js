// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

httpGetAsync('/topcontributordata', 'GET', function (result){
// Variables
  const labels = [];
  const values = [];

  // Parse JSON
  const json = JSON.parse(result);
  const maxValue = Math.ceil((json[0].storyPoints + 5) / 10) * 10;

  // Loop over Indicator Objects
  for (let i = 0; i < json.length; i++) {
    const obj = json[i];
    labels.push(obj.name);
    values.push(obj.storyPoints);
  }

  // Bar Chart Example
  const ctx = document.getElementById("myBarChart");
  const myBarChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: "Story Points",
        backgroundColor: "#4e73df",
        hoverBackgroundColor: "#2e59d9",
        borderColor: "#4e73df",
        data: values,
        maxBarThickness: 50
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
            unit: 'month'
          },
          gridLines: {
            display: false,
            drawBorder: false
          },
          ticks: {
            maxTicksLimit: 6
          }
        }],
        yAxes: [{
          ticks: {
            min: 0,
            max: maxValue,
            maxTicksLimit: 10,
            padding: 10,
            // Include a dollar sign in the ticks
            callback: function (value, index, values) {
              return value;
            }
          },
          gridLines: {
            color: "rgb(234, 236, 244)",
            zeroLineColor: "rgb(234, 236, 244)",
            drawBorder: false,
            borderDash: [2],
            zeroLineBorderDash: [2]
          }
        }],
      },
      legend: {
        display: false
      },
      tooltips: {
        titleMarginBottom: 10,
        titleFontColor: '#6e707e',
        titleFontSize: 14,
        backgroundColor: "rgb(255,255,255)",
        bodyFontColor: "#858796",
        borderColor: '#dddfeb',
        borderWidth: 1,
        xPadding: 15,
        yPadding: 15,
        displayColors: false,
        caretPadding: 10,
        callbacks: {
          label: function (tooltipItem, chart) {
            var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
            return datasetLabel + ': ' + tooltipItem.yLabel;
          }
        }
      },
    }
  });
});


