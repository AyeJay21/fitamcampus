package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowerService {
    public final FollowRequestRepository followRequestRepository;

    public FollowerService(FollowRequestRepository followRequestRepository){
        this.followRequestRepository = followRequestRepository;
    }

    public void saveOutsideFollowRequest(String username, String receiver, String type) {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setUsername(username);
        followRequest.setFollowerUrl(receiver);
        followRequest.setType(type);
        System.out.println("Request succesfully saved in followRequest");
        followRequestRepository.save(followRequest);
    }

    public List<FollowRequest> getFollowerRequest(String username){
        System.out.println("======== Get FollowerRequest ========");
        List<FollowRequest> requests = followRequestRepository.findByUsername(username);
        for (FollowRequest req : requests) {
            System.out.println("id=" + req.getId() + ", username=" + req.getUsername() + ", followerUrl=" + req.getFollowerUrl() + ", type=" + req.getType());
        }
        return requests;
    }
}
