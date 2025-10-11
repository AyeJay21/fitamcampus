package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping("/register")
    public String registerLink(){
        return "register";
    }

    @GetMapping("/events")
    public String eventLink(){
        return "events";
    }
}
