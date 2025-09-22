package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.Actor;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ActivityPubController {


    @Autowired
    private ActorService actorService;

    @Autowired
    private RemoteActorService remoteActorService;

    @Autowired
    private ActivityPubDeliveryService deliveryService;

    @PostMapping("/activitypub/send-follow")
    public ResponseEntity<?> sendFollow(@RequestParam String targetHandle) throws Exception {
        try {
            Actor me = actorService.getActorByUsername("ayejay");
            String privateKey = me.getPrivateKeyPem();
            String actorId = "https://activitypub.alluneedspot.com/users/" + me.getUsername();

            String targetInbox = remoteActorService.resolveActorInbox(targetHandle);

            Map<String, Object> follow = new HashMap<>();
            follow.put("@context", "https://www.w3.org/ns/activitystreams");
            follow.put("id", actorId + "/activity/follow-" + java.util.UUID.randomUUID());
            follow.put("type", "Follow");
            follow.put("actor", actorId);
            follow.put("object", targetHandle);

            deliveryService.sendToInbox(targetInbox, follow, actorId, privateKey);

            return ResponseEntity.ok(Map.of("success", true, "message", "Follow Activity sent!"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
