package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.*;

@Controller
public class OutboxController {

    public final OutboxRepository outboxRepository;

    public OutboxController(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(OutboxController.class);

    @Autowired
    public MessageService messageService;

    @GetMapping(value = "/users/{username}/outbox", produces = "application/activity+json")
    public ResponseEntity<?> getOutbox(@PathVariable String username) throws IOException {
        List<Outbox> items = outboxRepository.findAll();

        List<Map<String, Object>> orderedItems = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Outbox item : items) {
            orderedItems.add(mapper.readValue(item.getActivityJson(), Map.class));
        }

        Map<String, Object> outbox = new HashMap<>();
        outbox.put("@context", "https://www.w3.org/ns/activitystreams");
        outbox.put("id", "https://activitypub.alluneedspot.com/users/" + username + "/outbox");
        outbox.put("type", "OrderedCollection");
        outbox.put("totalItems", orderedItems.size());
        outbox.put("orderedItems", orderedItems);

        return ResponseEntity.ok(outbox);
    }

    @PostMapping(value = "/users/{username}/outbox",
            consumes = "application/json",
            produces = "application/activity+json")
    public ResponseEntity<String> postToOutbox(
            @PathVariable String username,
            @RequestBody Map<String, Object> activity) throws IOException {
        System.out.println("OUTBOX ACTIVITY: " + activity);
        logger.info("OUTBOX ACTIVITY: " + activity);
        logger.info("OUTBOX ACTIVITY Should be here");
        Outbox item = new Outbox();
        item.setActivity(activity);

            if ("Note".equals(activity.get("type").toString())) {
            String sender = (String) activity.getOrDefault("attributedTo", activity.get("actor"));
            Map<String, Object> objectMap = (Map<String, Object>) activity.get("object");
            String content = (String) objectMap.get("content");
            Object toObj = activity.get("to");
            String reciever = (toObj instanceof List<?> && !((List<?>)toObj).isEmpty()) ? ((List<?>)toObj).get(0).toString() : toObj.toString();
            // prefer top-level activity id if present
            String activityId = activity.get("id") != null ? activity.get("id").toString() : null;
            messageService.saveMessage(sender, reciever, content, new Date(), activityId);

        } else if ("Create".equals(activity.get("type")) && activity.get("object") instanceof Map) {
            Map<String, Object> objectMap = (Map<String, Object>) activity.get("object");
            String sender = (String) objectMap.getOrDefault("attributedTo", activity.get("actor"));
            String content = (String) objectMap.get("content");
            Object toObj = objectMap.get("to");
            if (toObj == null) {
                toObj = activity.get("to");
            }
            String reciever = (toObj instanceof List<?> && !((List<?>)toObj).isEmpty()) ? ((List<?>)toObj).get(0).toString() : toObj.toString();

            logger.info("[OUTBOX DEBUG] Sender: " + sender);
            logger.info("[OUTBOX DEBUG] Receiver: " + reciever);
            logger.info("[OUTBOX DEBUG] Content: " + content);
            // prefer top-level activity id (the Create activity id) if present, otherwise fall back to object id
            String activityId = activity.get("id") != null ? activity.get("id").toString() : (objectMap.get("id") != null ? objectMap.get("id").toString() : null);
            messageService.saveMessage(sender, reciever, content, new Date(), activityId);
        }

        outboxRepository.save(item);

        return ResponseEntity.status(201).body("Activity gespeichert");
    }
}