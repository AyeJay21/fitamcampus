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

        followRequestRepository.save(followRequest);
    }

    public List<FollowRequest> getFollowerRequest(String username){
        return followRequestRepository.findByUsername(username);
    }
}
