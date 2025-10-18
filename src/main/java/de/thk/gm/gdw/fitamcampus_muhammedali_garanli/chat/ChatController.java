package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.chat;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.navigation.LoginController;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChatController {

    @GetMapping("/users/chats")
    public String openChat(Model model, HttpSession session) throws Exception {
        String username = LoginController.getCurrentUser(session).getUsername();
        if(username == null || username.isEmpty()) {
           throw new Exception("Username is null or empty");
        }
        model.addAttribute("username", username);
        return "chat";
    }

}
