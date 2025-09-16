package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.Actor;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

public class Actor {

    public Map<String, Object> getActor(@PathVariable String username){
        Map<String, Object> actor = new HashMap<>();
        actor.put("@context", "https://www.w3.org/ns/activitystreams");
        actor.put("id", "https://myproject.example.com/users/" + username);
        actor.put("type", "Person");
        actor.put("preferredUsername", username);
        actor.put("inbox", "https://myproject.example.com/users/" + username + "/inbox");
        actor.put("outbox", "https://myproject.example.com/users/" + username + "/outbox");
        actor.put("followers", "https://myproject.example.com/users/" + username + "/followers");
        actor.put("following", "https://myproject.example.com/users/" + username + "/following");
        return actor;
    }
}
