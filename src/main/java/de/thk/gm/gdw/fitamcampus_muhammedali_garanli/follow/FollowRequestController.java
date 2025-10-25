package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class FollowRequestController {

    @Autowired
    FollowerService followerService;

    @GetMapping("/users/{username}/followRequests")
    public List<?> getFollowRequest(@PathVariable String username){
        return followerService.getFollowerRequest(username);
    }

    @GetMapping("/followerRequest/{username}")
    public String redirectFolloweRequest(@PathVariable String username, Model model){
        model.addAttribute("username", username);
        return "followerRequest";
    }
}
