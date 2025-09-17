package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.Actor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public class Inbox {

    @PostMapping(value = "/users/{username}/inbox", consumes = "application/activity+json")
    public ResponseEntity<String> postInbox(@PathVariable String username, @RequestBody Map<String, Object> body) {
        System.out.println("Inbox received for " + username + ": " + body);
        return ResponseEntity.accepted().body("Accepted");
    }
}