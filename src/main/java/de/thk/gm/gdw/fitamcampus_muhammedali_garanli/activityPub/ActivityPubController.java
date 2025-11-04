package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.Actor;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.ActorService;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.Message;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.MessageRepository;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.MessageService;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.outbox.Outbox;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.outbox.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.sse.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ActivityPubController {

    private static final Logger log = LoggerFactory.getLogger(ActivityPubController.class);

    @Autowired
    private ActorService actorService;

    @Autowired
    private RemoteActorService remoteActorService;

    @Autowired
    private ActivityPubDeliveryService deliveryService;

    @Autowired
    private OutboxRepository outboxRepository;
    @Autowired
    public MessageService messageService;

    @Autowired
    private SseService sseService;

    @PostMapping("/activitypub/send-follow")
    public ResponseEntity<?> sendFollow(
            @RequestBody SendFollowRequest request) throws Exception {
        try {
            String fromUser = request.getFromUser();
            String targetHandle = request.getTargetHandle();
            
            Actor me = actorService.getActorByUsername(fromUser);
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
            @RequestBody SendNoteRequest request) {
        try {
            String fromUser = request.getFromUser();
            String message = request.getMessage();
            String targetHandle = request.getTargetHandle();

            System.out.println("FROMUSER: " + fromUser);
            Actor me = actorService.getActorByUsername(fromUser);
            String privateKey = me.getPrivateKeyPem();
            System.out.println("ME: " + me);
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

            System.out.println("BackendEnd fromUser: " + fromUser + " targetHandle: " + targetHandle + " message: " + message);
            // Save message locally for remote recipients; for local recipients the InboxController will
            // persist the incoming activity when it is POSTed to /inbox, so avoid double-saving here.
            boolean isLocalRecipient = (targetInbox != null && targetInbox.contains("activitypub.alluneedspot.com"));
            if (!isLocalRecipient) {
                messageService.saveMessage(fromUser, targetActorUrl, message, new Date());
            } else {
                log.info("Local recipient detected ({}); skipping local save — inbox will persist it.", targetInbox);
            }

            // Only notify sender sessions here. Do NOT push to recipient room from the sender endpoint.
            try {
                Map<String, Object> payload = Map.of(
                    "sender", fromUser,
                    "text", message,
                    "timeStamp", new Date().getTime(),
                    "room", targetActorUrl,
                    "tempId", request.getTempId()
                );
                log.info("Pushing SSE payload to senderRoom={}; payload preview={}", actorId, message);
                sseService.pushToRoom(actorId, payload);
            } catch (Exception e) {
                log.warn("Failed to push SSE payload to sender room: {}", e.getMessage());
            }

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

    @PostMapping("/activitypub/send-private-message")
    public ResponseEntity<?> sendDirectMessage(
            @RequestBody SendNoteRequest request) {
        try {
            String fromUser = request.getFromUser();
            String message = request.getMessage();
            String targetHandle = request.getTargetHandle();
            Actor me = actorService.getActorByUsername(fromUser);
            String privateKey = me.getPrivateKeyPem();
            String actorId = "https://activitypub.alluneedspot.com/users/" + me.getUsername();

            String targetInbox = remoteActorService.resolveActorInbox(targetHandle);
            String targetActorUrl = remoteActorService.resolveActorUrl(targetHandle);

            // Private Erwähnung - nur an den Empfänger, aber mit Mention-Tag
            Map<String, Object> note = new HashMap<>();
            note.put("@context", "https://www.w3.org/ns/activitystreams");
            note.put("type", "Note");
            note.put("id", actorId + "/notes/" + java.util.UUID.randomUUID());
            note.put("content", message);
            note.put("attributedTo", actorId);
            note.put("to", Arrays.asList(targetActorUrl));
            // Erwähnung hinzufügen für bessere Sichtbarkeit
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
            createActivity.put("to", Arrays.asList(targetActorUrl));

            deliveryService.sendToInbox(targetInbox, createActivity, actorId, privateKey);

            Outbox outboxItem = new Outbox();
            outboxItem.setActivity(createActivity);
            outboxRepository.save(outboxItem);

            boolean isLocalRecipient = (targetInbox != null && targetInbox.contains("activitypub.alluneedspot.com"));
            if (!isLocalRecipient) {
                messageService.saveMessage(fromUser, targetActorUrl, message, new Date());
            } else {
                log.info("Detected local recipient ({}); skipping duplicate save for direct message.", targetInbox);
            }

            try {
                Map<String, Object> payload = Map.of(
                    "sender", fromUser,
                    "text", message,
                    "timeStamp", new Date().getTime(),
                    "room", targetActorUrl,
                    "tempId", request.getTempId()
                );
                // only notify sender sessions here; recipient will be notified via InboxController when it receives the activity
                sseService.pushToRoom(actorId, payload);
            } catch (Exception ignored) {}

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Direct message sent to " + targetHandle,
                "messageContent", message,
                "sentTo", targetInbox,
                "visibility", "private"
            ));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}