package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.WebFinger;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.ActorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WebFingerController {

    private final ActorRepository actorRepository;

    public WebFingerController(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @GetMapping(value = "/.well-known/webfinger", produces = "application/jrd+json")
    public ResponseEntity<?> webfinger(@RequestParam String resource) {
        if (!resource.startsWith("acct:")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid resource format"));
        }

        // resource sieht aus wie: acct:ayejay@alluneedspot.com
        String acct = resource.substring(5); // "ayejay@alluneedspot.com"
        String username = acct.split("@")[0]; // "ayejay"

        return actorRepository.findByUsername(username)
                .map(actor -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("subject", resource);

                    List<Map<String, Object>> links = new ArrayList<>();
                    Map<String, Object> self = new HashMap<>();
                    self.put("rel", "self");
                    self.put("type", "application/activity+json");
                    self.put("href", "https://activitypub.alluneedspot.com/users/" + username);
                    links.add(self);

                    response.put("links", links);

                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }
}
