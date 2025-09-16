package de.thk.gm.gdw.fitamcampus_muhammedali_garanli;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {

    @GetMapping("/meetingSite")
    public String greeting(){
        return "meetingSite";
    }
}
