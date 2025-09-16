package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeatherRestController {

    public final WeatherService weatherService;

    @Autowired
    public WeatherRestController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @PostMapping("/map")
    public String displayMap(){
        return "openStreetMapDisplay";
    }
    /*@GetMapping("/weathers")
    public ResponseEntity<Weather> getWeather() throws Exception {
        Weather weatherData = weatherService.getWeather();
        return ResponseEntity.ok(weatherData);
    }*/
}
