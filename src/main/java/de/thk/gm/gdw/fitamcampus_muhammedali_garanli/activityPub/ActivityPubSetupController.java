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
public class ActivityPubSetupController {
    @Autowired
    private ActorService actorService;

    /**
     * Erstellt einen neuen Actor mit RSA-Schlüsseln * Aufruf: POST /setup-actor?username=ayejay
     */
    @PostMapping("/setup-actor")
    public ResponseEntity<Map<String, Object>> setupActor(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        try {
            Actor actor = actorService.createActor(username);
            result.put("success", true);
            result.put("message", "Actor erfolgreich erstellt");
            result.put("username", actor.getUsername());
            result.put("actorId", "https://activitypub.alluneedspot.com/users/" + username);
            result.put("publicKeyId", "https://activitypub.alluneedspot.com/users/" + username + "#main-key");
            result.put("hasPrivateKey", actor.getPrivateKeyPem() != null);
            result.put("hasPublicKey", actor.getPublicKeyPem() != null);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
        return ResponseEntity.ok(result);
    }
}
