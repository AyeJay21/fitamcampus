package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.WebFinger;

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

    @GetMapping(value = ".well-known/webfinger", produces = "application/activity+json")
    public ResponseEntity<?> webfinger(@RequestParam String resource){
        if (!resource.equals("acct:ayejay@alluneedspot.com")) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("subject", "acct:ayejay@alluneedspot.com");

        List<Map<String, Object>> links = new ArrayList<>();
        Map<String, Object> self = new HashMap<>();
        self.put("rel", "self");
        self.put("type", "application/activity+json");
        self.put("href","https://activitypub.alluneedspot.com/users/ayejay");
        links.add(self);

        response.put("links", links);

        return ResponseEntity.ok(response);
    }
}
