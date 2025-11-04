package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FollowRequestController {

    @Autowired
    FollowerService followerService;

    @Autowired
    FollowRequestService followRequestService;

    @GetMapping("/users/{username}/followRequests")
    @ResponseBody
    public List<FollowRequest> getFollowRequest(@PathVariable String username){
        return followerService.getFollowerRequest(username);
    }

    @GetMapping("/followerRequest/{username}")
    public String redirectFollowRequest(@PathVariable String username, Model model){
        model.addAttribute("username", username);
        return "followerRequest";
    }

    @DeleteMapping("/users/{username}/followRequests/delete")
    public void deleteFollowRequest(@PathVariable String username , @RequestBody String followerUrl){
        if (followerUrl != null) {
            followerUrl = followerUrl.trim();
            // remove surrounding JSON quotes if present
            if (followerUrl.startsWith("\"") && followerUrl.endsWith("\"")) {
                followerUrl = followerUrl.substring(1, followerUrl.length() - 1);
            }
            followerUrl = followerUrl.trim();
        }
        followRequestService.deleteFromFollowRequest(username, followerUrl);
    }
}
