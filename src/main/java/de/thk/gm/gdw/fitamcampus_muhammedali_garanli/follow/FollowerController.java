package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub.ActivityPubDeliveryService;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub.RemoteActorService;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/{username}/followers")
public class FollowerController {
    @Autowired
    private FollowerRepository followerRepository;

        @Autowired
        private ActorService actorService;

        @Autowired
        private RemoteActorService remoteActorService;

        @Autowired
        private ActivityPubDeliveryService deliveryService;

    // Follower-Request annehmen und Accept-Activity senden
    @PostMapping("/accept")
    public ResponseEntity<?> acceptFollower(@PathVariable String username, @RequestBody Map<String, String> body) {
        String followerUrl = body.get("followerUrl");
        if (followerUrl == null || followerUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "followerUrl required"));
        }
        Follower follower = new Follower();
        follower.setUsername(username);
        follower.setFollowerUrl(followerUrl);
        followerRepository.save(follower);

        try {
            // Lokalen Actor holen
            var me = actorService.getActorByUsername(username);
            String actorId = "https://activitypub.alluneedspot.com/users/" + me.getUsername();
            String privateKey = me.getPrivateKeyPem();

            // Remote Inbox und Actor URL auflösen
            String targetInbox = remoteActorService.resolveActorInbox(followerUrl);
            String targetActorUrl = remoteActorService.resolveActorUrl(followerUrl);

            // Accept-Aktivität bauen
            java.util.Map<String, Object> accept = new java.util.HashMap<>();
            accept.put("@context", "https://www.w3.org/ns/activitystreams");
            accept.put("id", actorId + "/activities/accept-" + java.util.UUID.randomUUID());
            accept.put("type", "Accept");
            accept.put("actor", actorId);
            // Das Objekt ist die ursprüngliche Follow-Aktivität
            java.util.Map<String, Object> followObj = new java.util.HashMap<>();
            followObj.put("type", "Follow");
            followObj.put("actor", targetActorUrl);
            followObj.put("object", actorId);
            accept.put("object", followObj);

            // Senden
            deliveryService.sendToInbox(targetInbox, accept, actorId, privateKey);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Follower accepted, but failed to federate: " + e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("success", true, "followerUrl", followerUrl));
    }

    // Alle Follower anzeigen
    @GetMapping
    public ResponseEntity<?> getFollowers(@PathVariable String username) {
        List<Follower> followers = followerRepository.findByUsername(username);
        return ResponseEntity.ok(followers);
    }
}
