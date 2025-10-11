package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.Actor;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ActivityPubSetupController {
    @Autowired
    private ActorService actorService;

    @PostMapping(value = "/setup-actor", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setupActorJson(@RequestParam("username") String username,
                                                              @RequestParam("email") String email,
                                                              @RequestParam("password") String password) {

        Map<String, Object> result = new HashMap<>();
        try {
            Actor actor = actorService.createActor(username, email, password);
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

    @PostMapping(value = "/setup-actor", consumes = "application/x-www-form-urlencoded")
    public String setupActorForm(@RequestParam("username") String username,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password) {
        try {
            actorService.createActor(username, email, password);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/register?error=" + e.getMessage();
        }
    }
}
