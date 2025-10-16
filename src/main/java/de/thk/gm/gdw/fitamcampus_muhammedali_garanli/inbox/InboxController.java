package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.inbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.Message;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/users/{username}/inbox")
public class InboxController {

    public final InboxRepository inboxRepository;

    public InboxController(InboxRepository inboxRepository) {
        this.inboxRepository = inboxRepository;
    }

    @Autowired
    public MessageService messageService;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Inbox> getInbox(@PathVariable String username) {
        return inboxRepository.findByUsername(username);
    }

    @PostMapping(consumes = {"application/json", "application/activity+json"})
    @ResponseBody
    public Map<String, Object> addToInbox(@PathVariable String username, @RequestBody Map<String, Object> activity) throws Exception {
        System.out.println("Empfangene Activity: " + activity);

        Inbox inbox = new Inbox();
        Message message = new Message();
        ObjectMapper mapper = new ObjectMapper();
        String reciever = "";

        inbox.setActivity(activity);
        inbox.setUsername(username);
        String type = (String) activity.get("type");
        inbox.setType(type);
        inbox.setActor((String) activity.get("actor"));

        inbox.setObjectData(activity.get("object").toString());
        if (activity.get("object") instanceof Map<?, ?> objectMap) {
            inbox.setObjectData(mapper.writeValueAsString(objectMap));
            inbox.setObjectType((String) objectMap.get("type"));
            inbox.setContent((String) objectMap.get("content"));
            inbox.setAttributedTo((String) objectMap.get("attributedTo"));
            inbox.setPublished((String) objectMap.get("published"));
            inbox.setTo((objectMap.get("to") != null) ? objectMap.get("to").toString() : null);

            if ("Create".equals(type) && "Note".equals(objectMap.get("type"))) {
                String sender = (String) activity.get("actor");
                String receiver = null;

                Object toObj = objectMap.get("to");
                if (toObj instanceof List<?> toList && !toList.isEmpty()) {
                    receiver = toList.get(0).toString();
                } else if (toObj instanceof String s) {
                    receiver = s;
                }

                String text = (String) objectMap.get("content");
                Date date = null;
                if (objectMap.get("published") != null) {
                    date = Date.from(Instant.parse(objectMap.get("published").toString()));
                }

                messageService.saveMessage(sender, receiver, text, date);
            }
        }

        Inbox saved = inboxRepository.save(inbox);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Activity received");
        response.put("activityType", type != null ? type : "");
        response.put("from", inbox.getActor() != null ? inbox.getActor() : "");
        response.put("objectType", inbox.getObjectType() != null ? inbox.getObjectType() : "");
        response.put("content", inbox.getContent() != null ? inbox.getContent() : "");
        return response;
    }
}