package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.Actor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class Outbox {

    @GetMapping(value = "/users/{username}/outbox", produces = "application/activity+json")
    public Map<String, Object> getOutbox(@PathVariable String username) {
        Map<String, Object> outbox = new HashMap<>();
        outbox.put("@context", "https://www.w3.org/ns/activitystreams");
        outbox.put("id", "https://activitypub.alluneedspot.com/users/" + username + "/outbox");
        outbox.put("type", "OrderedCollection");
        outbox.put("totalItems", 0);
        outbox.put("orderedItems", new ArrayList<>());
        return outbox;
    }
}
