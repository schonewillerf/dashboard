// Put city + apiKey in requestUri
var request = new XMLHttpRequest();
var city = "Amersfoort";
var apiKey = "3a7724b5c33a53b756d9aaeb997c3527";
var requestUri = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=" + apiKey;

request.open("GET", requestUri);
request.send();

// Wait for HTTP response
request.onreadystatechange = function () {

    // Typical action to be performed when the document is ready:
    if (this.readyState === 4 && this.status === 200) {
        var obj = JSON.parse(request.responseText);
        getImgSrc(obj);
        getDataFromApi(obj);
        getCity(obj);
    }
}

// Set image src in html
function getImgSrc(responseObject) {
    var weather = responseObject.weather;
    var objectWeather = weather[0];
    var id = objectWeather.id;
    var imgCode = getImageCode(id);
    var imgSrc = "http://openweathermap.org/img/wn/" + imgCode + "@4x.png";
    document.getElementById("weerImg").style.backgroundImage = 'url(' + imgSrc + ')';

}

// Find image code by description id. Code of image is connected with id of weather description
function getImageCode(id) {
    switch (id) {
        case 800:
            return "01d";
        case 801:
            return "02d";
        case 802:
            return "03d";
        case 803:
            return "04d";
        case 521:
            return "09d";
        case 500:
            return "10d";
        case 601:
            return "13d";
        case 200:
            return "11d";
        case 701:
            return "50d";
        case 804:
            return "04d";
        default:
            return "10d";
    }
}

// Get temperatures, wind speed and humidity
function getDataFromApi(obj) {
    var objMain = obj.main;
    var items = Object.keys(objMain);

    items.map(key => {

        // Temperature
        var temp = Math.round(objMain.temp - 273);
        document.getElementById("temp").textContent = temp + "℃";

        // Wind speed
        var wind = obj.wind;
        var windSpeed = wind.speed;
        document.getElementById("windSpeed").textContent = windSpeed + "m/s";

        // Humidity
        var humidity = objMain.humidity;
        document.getElementById("airHumidity").textContent = humidity + "%";
    });
}

// Get city location
function getCity(responseObject) {
    document.getElementById("city").textContent = responseObject.name;
}
