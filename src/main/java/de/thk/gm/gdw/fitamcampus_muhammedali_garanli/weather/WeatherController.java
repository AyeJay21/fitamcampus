package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WeatherController {

    public final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @RequestMapping(value = "/map", method = {RequestMethod.GET, RequestMethod.POST})
    public String displayMap(){
        return "openStreetMapDisplay";
    }

    /*@GetMapping("/weathers")
    public String getWeather(Model model) throws Exception {
        Weather weatherData = weatherService.getWeather();
        model.addAttribute("weatherData",weatherData);
        return "redirect:/meetings/{meetingId}";
    }*/
}
