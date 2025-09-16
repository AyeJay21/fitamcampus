package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.weather;

import org.springframework.stereotype.Service;

import java.time.LocalDate;


public interface WeatherService {
    Weather getWeather(LocalDate date, String ort, String uhrzeit) throws Exception;
}
