package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.chat;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.Message;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.MessageRepository;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.navigation.LoginController;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @GetMapping("/users/chats")
    public String openChat(Model model, HttpSession session) throws Exception {
        String username = LoginController.getCurrentUser(session).getUsername();
        if(username == null || username.isEmpty()) {
           throw new Exception("Username is null or empty");
        }
        model.addAttribute("username", username);
        return "chat";
    }

    @GetMapping("/users/{username}/chats")
    public String getChat(@PathVariable String username, @RequestBody String receiver, Model model){
        List<Message> allMessages = messageRepository.findAll();
        String userA = username;
        String userB = receiver;

        if (receiver != null && !receiver.isBlank()) {
            List<Message> chatMessages = allMessages.stream()
                    .filter(m -> (m.getSender().equals(userA) && m.getReciever().equals(userB)) ||
                            (m.getSender().equals(userB) && m.getReciever().equals(userA)))
                    .sorted(Comparator.comparing(Message::getTimeStamp))
                    .collect(Collectors.toList());

            model.addAttribute("chatMessages", chatMessages);
        }
        return "chat";
    }
}
