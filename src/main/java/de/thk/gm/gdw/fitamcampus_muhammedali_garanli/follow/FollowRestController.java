package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FollowRestController {

    @Autowired
    private FollowerRepository followerRepository;

    //    @GetMapping
    //    public List<Follower> getFollowers(@PathVariable String username, Model model) {
    //        List<Follower> followers = followerRepository.findByUsername(username);
    //        model.addAttribute("followers", followers);
    //        return followers;
    //    }
    @ResponseBody
    @GetMapping("/users/{username}/followers")
    public List<Follower> getFollowers(@PathVariable String username, Model model) {
        List<Follower> followers = followerRepository.findByUsername(username);
        model.addAttribute("followers", followers);
        return followers;
    }
}
