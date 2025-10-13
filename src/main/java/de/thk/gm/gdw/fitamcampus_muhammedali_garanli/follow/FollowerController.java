package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

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

    // Follower-Request annehmen
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
        return ResponseEntity.ok(Map.of("success", true, "followerUrl", followerUrl));
    }

    // Alle Follower anzeigen
    @GetMapping
    public ResponseEntity<?> getFollowers(@PathVariable String username) {
        List<Follower> followers = followerRepository.findByUsername(username);
        return ResponseEntity.ok(followers);
    }
}
