package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActorController {
    public final ActorRepository actorRepository;

    public ActorController(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @GetMapping(value = "/users/{username}", produces = "application/activity+json")
    public ResponseEntity<?> getActor(@PathVariable String username) {
        return actorRepository.findByUsername(username)
                .map(actor -> {
                            Map<String, Object> json = new HashMap<>();
                            json.put("@context", "https://www.w3.org/ns/activitystreams");
                            json.put("type", "Person");
                            json.put("id", "https://activitypub.alluneedspot.com/users/" + username);
                            json.put("preferredUsername", username);
                            json.put("inbox", "https://activitypub.alluneedspot.com/users/" + username + "/inbox");
                            json.put("outbox", "https://activitypub.alluneedspot.com/users/" + username + "/outbox");
                            json.put("followers", "https://activitypub.alluneedspot.com/users/" + username + "/followers");
                            json.put("following", "https://activitypub.alluneedspot.com/users/" + username + "/following");

                            Map<String, Object> publicKey = new HashMap<>();
                            publicKey.put("id", "https://activitypub.alluneedspot.com/users/" + username + "#main-key");
                            publicKey.put("owner", "https://activitypub.alluneedspot.com/users/" + username);
                            publicKey.put("publicKeyPem", actor.getPublicKeyPem());

                            json.put("publicKey", publicKey);

                            return ResponseEntity.ok(json);
                        }
                )
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Actor not found")));
    }

    @GetMapping(value = "/users/{username}/followers", produces = "application/activity+json")
    public ResponseEntity<?> getFollowers(@PathVariable String username) {
        List<String> followers = List.of(
                "https://mastodon.social/users/AyeJay21",
                "https://pixelfed.de/users/AyeJay21"
        );
        Map<String, Object> response = new HashMap<>();
        response.put("@context", "https://www.w3.org/ns/activitystreams");
        response.put("id", "https://activitypub.alluneedspot.com/users/" + username + "/followers");
        response.put("type", "OrderedCollection");
        response.put("totalItems", followers.size());
        response.put("orderedItems", followers);
        return ResponseEntity.ok(response);
    }
}
