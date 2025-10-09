package de.thk.gm.gdw.fitamcampus_muhammedali_garanli;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.auth.LoginController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class GreetingController {

    @GetMapping("/meetingSite")
    public String greeting(HttpSession session, Model model){
        // Login-Check
        if (!LoginController.isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        model.addAttribute("currentUser", LoginController.getCurrentUser(session));
        return "meetingSite";
    }
}
