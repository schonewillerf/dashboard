// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

function number_format(number, decimals, dec_point, thousands_sep) {
  // *     example: number_format(1234.56, 2, ',', ' ');
  // *     return: '1 234,56'
  number = (number + '').replace(',', '').replace(' ', '');
  var n = !isFinite(+number) ? 0 : +number,
    prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
    sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
    dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
    s = '',
    toFixedFix = function(n, prec) {
      var k = Math.pow(10, prec);
      return '' + Math.round(n * k) / k;
    };
  // Fix for IE parseFloat(0.55).toFixed(0) = 0;
  s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
  if (s[0].length > 3) {
    s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
  }
  if ((s[1] || '').length < prec) {
    s[1] = s[1] || '';
    s[1] += new Array(prec - s[1].length + 1).join('0');
  }
  return s.join(dec);
}
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

  // Summary bar chart
  var ctx = document.getElementById("summaryChart");
  var myBarChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ["", "", "", ""],
      datasets: [{
        label: "Story Points",
        backgroundColor: myColorsArray,
        hoverBackgroundColor: myHoverColorsArray,
        borderColor: myColorsArray,
        data: values,
        maxBarThickness: 75
      }],
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
            display: false,
            reverse: true,
            min: 0,
            max: maxValue,
            maxTicksLimit: 10,
            padding: 10,
            // Include a dollar sign in the ticks
            callback: function (value, index, values) {
              return number_format(value);
            }
          },
          gridLines: {
            display: false
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
            return datasetLabel + ': ' + number_format(tooltipItem.yLabel);
          }
        }
      },
    }
  });
}
