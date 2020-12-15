// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

function summaryChart(json){
  //Loop though data and make arrays
  const values = [];
  const myColorsArray = ["#e74a3b", "#fd7e14", "#f6c23e", "#1cc88a"];

  for (let i = 0; i < json.length; i++) {
    const obj = json[i];
    values.push({
      label: obj.name,
      backgroundColor: myColorsArray[i],
      data: [obj.storyPoints],
      maxBarThickness: 50,
    })
  }

  const ctx = document.getElementById("summaryChart");
  new Chart(ctx, {
    type: 'horizontalBar',
    data: {
      datasets: values,
    },
    options: {
      maintainAspectRatio: false,
      layout: {
        padding: {
          left: 0,
          right: 0,
          top: 0,
          bottom: 0
        }
      },
      scales: {
        xAxes: [{
          stacked: true,
          ticks: {
            display: false
          },
          gridLines: {
            display: false,
            drawBorder: false
          }
        }],
        yAxes: [{
          stacked: true,
          gridLines: false,
        }]
      },
      legend: {
        display: false
      },
      tooltips: {
        enabled: false
      }
    }
  });
}
