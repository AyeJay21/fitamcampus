package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.Actor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

public class Actor {

    @GetMapping(value = "/users/{username}", produces = "application/activity+json")
    public Map<String, Object> getActor(@PathVariable String username) {
        Map<String, Object> actor = new HashMap<>();
        actor.put("@context", "https://www.w3.org/ns/activitystreams");
        actor.put("type", "Person");
        actor.put("id", "https://activitypub.alluneedspot.com/users/" + username);
        actor.put("preferredUsername", username);
        actor.put("inbox", "https://activitypub.alluneedspot.com/users/" + username + "/inbox");
        actor.put("outbox", "https://activitypub.alluneedspot.com/users/" + username + "/outbox");
        actor.put("followers", "https://activitypub.alluneedspot.com/users/" + username + "/followers");
        actor.put("following", "https://activitypub.alluneedspot.com/users/" + username + "/following");
        return actor;
    }
}
