package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages(){
        List<Message> messages = messageRepository.findAll();
        return ResponseEntity.ok(messages);
    }
}
