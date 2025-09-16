package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.weather;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherServiceImpl implements WeatherService {
    @Override
    public Weather getWeather(LocalDate date, String ort, String uhrzeit) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET()
                .uri(URI.create("https://nominatim.openstreetmap.org/search?q=" + ort + "&format=json")).build();
        HttpResponse<String> geocodingResponse = client.send(geocodingRequest, HttpResponse.BodyHandlers.ofString());
        String geocodingResponseBody = geocodingResponse.body();

        JSONArray geocodingJsonArray = new JSONArray(geocodingResponseBody);
        if (geocodingJsonArray.isEmpty()) {
            throw new Exception("Ort nicht gefunden");
        }
        JSONObject geocodingJson = geocodingJsonArray.getJSONObject(0);
        double latitude = geocodingJson.getDouble("lat");
        double longitude = geocodingJson.getDouble("lon");

        HttpRequest weatherRequest = HttpRequest.newBuilder().GET()
                .uri(URI.create("https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&start_date=" + date + "&end_date=" + date + "&minutely_15=temperature_2m,weather_code&hourly=temperature_2m")).build();
        HttpResponse<String> weatherResponse = client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
        JSONObject weatherJson = new JSONObject(weatherResponse.body());

        Weather weather = new Weather();
        String weatherType = "";
        JSONObject minutely = weatherJson.getJSONObject("minutely_15");
        JSONArray timeArray = minutely.getJSONArray("time");
        JSONArray tempArray = minutely.getJSONArray("temperature_2m");
        JSONArray weatherCodeArray = minutely.getJSONArray("weather_code");

        LocalTime requestedTime = LocalTime.parse(uhrzeit, DateTimeFormatter.ofPattern("HH:mm"));
        int index = -1;
        for (int i = 0; i < timeArray.length(); i++) {
            LocalTime apiTime = LocalTime.parse(timeArray.getString(i).substring(11, 16));
            if (apiTime.equals(requestedTime) || apiTime.isAfter(requestedTime.minusMinutes(7)) && apiTime.isBefore(requestedTime.plusMinutes(8))) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new Exception("Uhrzeit nicht gefunden");
        }
        double temp = tempArray.getDouble(index);
        int weatherCode = weatherCodeArray.getInt(index);
        if (weatherCode == 0) {
            weatherType = "Clear Sky";
        } else if (weatherCode >= 1 && weatherCode <= 3) {
            weatherType = "A little bit clear Sky";
        } else if (weatherCode == 45 || weatherCode == 48) {
            weatherType = "Cloudy ☁️";
        } else if (weatherCode == 51 || weatherCode == 53 || weatherCode == 55) {
            weatherType = "Drizzle 🌧️";
        } else if (weatherCode == 61 || weatherCode == 63 || weatherCode == 65 || weatherCode == 66 || weatherCode == 67) {
            weatherType = "Rainy 🌧️";
        } else {
            weatherType = "Clear Sky";
        }
        System.out.println("WeatherCode: " + weatherCode);
        System.out.println(temp + "°C");
        weather.temperature = temp;
        weather.weatherType = weatherType;
        weather.latitude = latitude;
        weather.longitude = longitude;
        return weather;
    }
}