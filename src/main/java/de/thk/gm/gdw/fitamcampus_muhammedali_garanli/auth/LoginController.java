package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.auth;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.Actor;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    @Autowired
    private ActorService actorService;
    
    // GET für Login-Seite
    @GetMapping("/login")
    public String showLoginPage(Model model, 
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "message", required = false) String message) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "login";
    }
    
    // POST für Login-Verarbeitung
    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email, 
                             @RequestParam("password") String password,
                             HttpSession session,
                             Model model) {
        try {
            Actor actor = actorService.login(email, password);
            
            // Session setzen
            session.setAttribute("loggedInUser", actor);
            session.setAttribute("userId", actor.getId());
            session.setAttribute("username", actor.getUsername());
            
            return "redirect:/meetingSite";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
    
    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?message=Erfolgreich ausgeloggt";
    }
    
    // Helper Methode um zu prüfen ob User eingeloggt ist
    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }
    
    // Helper Methode um aktuellen User zu holen
    public static Actor getCurrentUser(HttpSession session) {
        return (Actor) session.getAttribute("loggedInUser");
    }
}