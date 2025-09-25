package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.Actor;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.ActorService;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.outbox.Outbox;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.outbox.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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

    @Autowired
    private OutboxRepository outboxRepository;

    @PostMapping("/activitypub/send-follow")
    public ResponseEntity<?> sendFollow(@RequestParam String targetHandle) throws Exception {
        try {
            Actor me = actorService.getActorByUsername("ayejay");
            String privateKey = me.getPrivateKeyPem();
            String actorId = "https://activitypub.alluneedspot.com/users/" + me.getUsername();

            String targetInbox = remoteActorService.resolveActorInbox(targetHandle);
            String targetActorUrl = remoteActorService.resolveActorUrl(targetHandle);

            Map<String, Object> follow = new HashMap<>();
            follow.put("@context", "https://www.w3.org/ns/activitystreams");
            follow.put("id", actorId + "/activities/follow-" + java.util.UUID.randomUUID());
            follow.put("type", "Follow");
            follow.put("actor", actorId);
            follow.put("object", targetActorUrl);

            deliveryService.sendToInbox(targetInbox, follow, actorId, privateKey);

            return ResponseEntity.ok(Map.of("success", true, "message", "Follow Activity sent!"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/activitypub/send-note") 
    public ResponseEntity<?> sendNote(
            @RequestParam String targetHandle, 
            @RequestParam String message) {
        try {
            
            Actor me = actorService.getActorByUsername("ayejay");
            String privateKey = me.getPrivateKeyPem();
            String actorId = "https://activitypub.alluneedspot.com/users/" + me.getUsername();

            String targetInbox = remoteActorService.resolveActorInbox(targetHandle);

            String targetActorUrl = remoteActorService.resolveActorUrl(targetHandle);

            Map<String, Object> note = new HashMap<>();
            note.put("@context", "https://www.w3.org/ns/activitystreams");
            note.put("type", "Note");
            note.put("id", actorId + "/notes/" + java.util.UUID.randomUUID());
            note.put("content", message);
            note.put("attributedTo", actorId);
            note.put("to", Arrays.asList(targetActorUrl, "https://www.w3.org/ns/activitystreams#Public"));
            note.put("cc", Arrays.asList("https://www.w3.org/ns/activitystreams#Public"));
            note.put("tag", Arrays.asList(Map.of(
                "type", "Mention",
                "href", targetActorUrl,
                "name", targetHandle
            )));

            Map<String, Object> createActivity = new HashMap<>();
            createActivity.put("@context", "https://www.w3.org/ns/activitystreams");
            createActivity.put("id", actorId + "/activities/create-" + java.util.UUID.randomUUID());
            createActivity.put("type", "Create");
            createActivity.put("actor", actorId);
            createActivity.put("object", note);
            createActivity.put("to", Arrays.asList(targetActorUrl, "https://www.w3.org/ns/activitystreams#Public"));

            deliveryService.sendToInbox(targetInbox, createActivity, actorId, privateKey);

            Outbox outboxItem = new Outbox();
            outboxItem.setActivity(createActivity);
            outboxRepository.save(outboxItem);

            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "Note sent to " + targetHandle,
                "noteContent", message,
                "sentTo", targetInbox
            ));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/activitypub/create-public-post") 
    public ResponseEntity<?> createPublicPost(@RequestParam String message) {
        try {
            Actor me = actorService.getActorByUsername("ayejay");
            String privateKey = me.getPrivateKeyPem();
            String actorId = "https://activitypub.alluneedspot.com/users/" + me.getUsername();

            Map<String, Object> note = new HashMap<>();
            note.put("@context", "https://www.w3.org/ns/activitystreams");
            note.put("type", "Note");
            note.put("id", actorId + "/notes/" + java.util.UUID.randomUUID());
            note.put("content", message);
            note.put("attributedTo", actorId);
            note.put("to", Arrays.asList("https://www.w3.org/ns/activitystreams#Public"));
            note.put("cc", Arrays.asList("https://activitypub.alluneedspot.com/users/" + me.getUsername() + "/followers"));

            Map<String, Object> createActivity = new HashMap<>();
            createActivity.put("@context", "https://www.w3.org/ns/activitystreams");
            createActivity.put("id", actorId + "/activities/create-" + java.util.UUID.randomUUID());
            createActivity.put("type", "Create");
            createActivity.put("actor", actorId);
            createActivity.put("object", note);
            createActivity.put("to", Arrays.asList("https://www.w3.org/ns/activitystreams#Public"));
            createActivity.put("cc", Arrays.asList("https://activitypub.alluneedspot.com/users/" + me.getUsername() + "/followers"));

            String sharedInbox = "https://mastodon.social/inbox";
            deliveryService.sendToInbox(sharedInbox, createActivity, actorId, privateKey);

            Outbox outboxItem = new Outbox();
            outboxItem.setActivity(createActivity);
            outboxRepository.save(outboxItem);

            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "Public post created!",
                "postContent", message,
                "sentTo", sharedInbox,
                "type", "public"
            ));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
