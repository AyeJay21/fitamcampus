package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.chat;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.Message;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.MessageRepository;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.navigation.LoginController;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/users/chats")
    public String openChat(Model model, HttpSession session) throws Exception {
        String username = LoginController.getCurrentUser(session).getUsername();
        if (username == null || username.isEmpty()) {
            throw new Exception("Username is null or empty");
        }
        model.addAttribute("username", username);
        return "chat";
    }

    @GetMapping("/users/{username}/chats")
    @ResponseBody
    public List<?> getChat(@PathVariable String username,
                          @RequestParam(required = false) String receiver,
                          Model model) {
        model.addAttribute("username", username);
        List<Message> allMessages = messageRepository.findAll();

        // Hilfsfunktion: name@domain zu https://domain/users/name
        Function<String, String> toUrl = (str) -> {
            if (str != null && str.contains("@")) {
                String[] parts = str.split("@");
                if (parts.length == 2) {
                    return "https://" + parts[1] + "/users/" + parts[0];
                }
            }
            return str;
        };

        List<Message> chatMessages = Collections.emptyList();
        if (receiver != null && !receiver.isBlank()) {
            String receiverUrl = receiver;
            final String receiverShort;
            // Falls receiver eine URL ist, baue name@domain
            if (receiverUrl.startsWith("https://") && receiverUrl.contains("/users/")) {
                String[] urlParts = receiverUrl.split("/users/");
                if (urlParts.length == 2) {
                    receiverShort = urlParts[1] + "@" + urlParts[0].replace("https://", "").replace("/", "");
                } else {
                    receiverShort = null;
                }
            } else {
                receiverShort = null;
            }

            // Hilfsfunktionen für alle Varianten des eigenen Users
            String usernameUrl = username.contains("@") ? toUrl.apply(username) : "https://activitypub.alluneedspot.com/users/" + username;
            String usernameShort = username.contains("@") ? username : null;

            chatMessages = allMessages.stream()
                .filter(m -> {
                    String senderUrl = toUrl.apply(m.getSender());
                    String recieverUrl = toUrl.apply(m.getReciever());
                    // Vergleiche alle Varianten: plain, URL, name@domain
                    boolean isOwnSender = m.getSender().equals(username) || senderUrl.equals(usernameUrl) || (usernameShort != null && m.getSender().equals(usernameShort));
                    boolean isOwnReceiver = m.getReciever().equals(username) || recieverUrl.equals(usernameUrl) || (usernameShort != null && m.getReciever().equals(usernameShort));
                    boolean isReceiverMatch = m.getSender().equals(receiver) || senderUrl.equals(receiverUrl) || (receiverShort != null && m.getSender().equals(receiverShort));
                    boolean isSenderMatch = m.getReciever().equals(receiver) || recieverUrl.equals(receiverUrl) || (receiverShort != null && m.getReciever().equals(receiverShort));

                    return ((isOwnSender && isSenderMatch) || (isOwnReceiver && isReceiverMatch));
                })
                .sorted(Comparator.comparing(Message::getTimeStamp))
                .collect(Collectors.toList());
        }
        for (Message message: chatMessages) {
            System.out.println(message.getText());
        }
        model.addAttribute("chatMessages", chatMessages);
        return chatMessages;
    }
}
