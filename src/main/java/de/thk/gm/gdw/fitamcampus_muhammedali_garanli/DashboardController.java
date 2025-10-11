package de.thk.gm.gdw.fitamcampus_muhammedali_garanli;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.navigation.LoginController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String dashboard(HttpSession session, Model model){
        if (!LoginController.isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        model.addAttribute("currentUser", LoginController.getCurrentUser(session));
        return "meetingSite";
    }

    @GetMapping("/meetingSite")
    public String meetingSiteRedirect(){
        return "redirect:/";
    }
}
