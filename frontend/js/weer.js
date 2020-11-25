var request = new XMLHttpRequest();
var city="Amersfoort";
var apiKey="3a7724b5c33a53b756d9aaeb997c3527";
var requestUri="http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID="+apiKey;

request.open("GET", requestUri, false);
request.send();
var status = request.status;
if(status==200) {
    var obj = JSON.parse(request.responseText);
    getImgSrc(obj);
    getDataFromApi(obj);
    getCity(obj);

}
else if(status==404)
    console.log("not found")
else
    console.log(request.statusText);



// set image src in html

function getImgSrc( responseObject) {
    var weather=responseObject.weather;
    var objectWeather=weather[0];
    var id=objectWeather.id;
    var imgCode =getImageCode(id);
    var imgSrc="http://openweathermap.org/img/wn/"+imgCode+"@2x.png";
    document.getElementById("weerImg").src = imgSrc;

}
//find image code by descrription id. Code of image is connected with id of weather description
function getImageCode(id) {

    switch(id) {
        case 800:
            return "01d";
            break;
        case 801:
            return "02d";
            break;
        case 802:
            return "03d";
            break;
        case 803:
            return "04d";
            break;
        case 521:
            return "09d";
            break;
        case 500:
            return "10d";
            break;
        case 601:
            return "13d";
            break;
        case 200:
            return "11d";
            break;
        case 701:
            return "50d";
            break;
        case 804:
            return "04d";
            break;
        default:
            return "10d";

    }
}

// get temperature, humidity and wind speed

function getDataFromApi(responseObject)
{

    var objMain=obj.main;
    console.log(objMain);
    var items = Object.keys(objMain);
    //
    items.map(key => {
        let value = objMain[key];
        //temperature
        var temp_max=Math.round(objMain.temp_max-273);
        var temp_min=Math.round(objMain.temp_min-273);
        var temp=Math.round(objMain.temp-273);
        document.getElementById("min_temp").innerHTML = temp_min+"℃";
        document.getElementById("max_temp").innerHTML = temp_max+"℃";
        // wind speed
        var wind=obj.wind;
        var windSpeed=wind.speed;
        document.getElementById("windSpeed").innerHTML = windSpeed+"m/s";
        //airHumidity
        var humidity=objMain.humidity;
        document.getElementById("airHumidity").innerHTML = humidity+"%";
    });
}
// get location
function getCity(responseObject)
{
    console.log("city:");
    var city=responseObject.name;
    console.log(city);
    document.getElementById("city").innerHTML = city;
}
