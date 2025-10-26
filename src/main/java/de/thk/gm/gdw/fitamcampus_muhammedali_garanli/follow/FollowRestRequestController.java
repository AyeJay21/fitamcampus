package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FollowRestRequestController {

    @Autowired
    public FollowerService followerService;

    @GetMapping("/users/{username}/followRequests")
    public ResponseEntity<List<FollowRequest>> getFollowRequest(@PathVariable String username){
        List<FollowRequest> requests = followerService.getFollowerRequest(username);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/followerRequest/{username}")
    public String redirectFolloweRequest(@PathVariable String username, Model model){
        model.addAttribute("username", username);
        return "followerRequest";
    }
}
