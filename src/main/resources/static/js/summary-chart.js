// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

function summaryChart(json){
  //Loop though data and make arrays
  var values = new Array();
  var maxValue = 0;

  for (var i = 0; i < json.length; i++) {
    var obj = json[i];
    values.push(obj.storyPoints);
  }

  maxValue = Math.max.apply(Math, values);

  //Custom bar colors
  var myColorsArray = ["#e74a3b", "#fd7e14", "#f6c23e", "#1cc88a"];
  var myHoverColorsArray = ["rgba(231, 74, 59, 0.7)", "rgba(253, 126, 20, 0.7)", "rgba(246, 194, 62, 0.7)", "rgba(28, 200, 138, 0.7)"];

  var ctx = document.getElementById("summaryChart");
  var thickness = 50;
  var stackedBar = new Chart(ctx, {
    type: 'horizontalBar',
    data: {
      labels: [''],
      datasets: [{
        label: 'Sprint Backlog',
        backgroundColor: myColorsArray[0],
        data: [values[0]],
        maxBarThickness: thickness
      },
      {
        label: 'Doing',
        backgroundColor: myColorsArray[1],
        data: [values[1]],
        maxBarThickness: thickness
      },
      {
        label: 'Testing',
        backgroundColor: myColorsArray[2],
        data: [values[2]],
        maxBarThickness: thickness
      },
      {
        label: 'Done',
        backgroundColor: myColorsArray[3],
        data: [values[3]],
        maxBarThickness: thickness
      }
      ]
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
      }
    }
  });
}
