package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.Actor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OutboxController {

    @Autowired
    OutboxRepository outboxRepository;

    @GetMapping(value = "/users/{username}/outbox", produces = "application/activity+json")
    public ResponseEntity<?> getOutbox(@PathVariable String username) throws IOException {
        List<Outbox> items = outboxRepository.findAll();

        List<Map<String,Object>> orderedItems = new ArrayList<>();
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
        Outbox item = new Outbox();
        item.setActivity(activity);
        outboxRepository.save(item);

        return ResponseEntity.status(201).body("Activity gespeichert");
    }
}
