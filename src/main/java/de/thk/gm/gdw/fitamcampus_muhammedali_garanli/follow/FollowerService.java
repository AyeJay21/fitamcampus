package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.stereotype.Service;

@Service
public class FollowerService {
    public final FollowRequestRepository followRequestRepository;

    public FollowerService(FollowRequestRepository followRequestRepository){
        this.followRequestRepository = followRequestRepository;
    }

    public void saveOutsideFollowRequest(String username, String receiver) {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setUsername(username);
        followRequest.setFollowerUrl(receiver);

        followRequestRepository.save(followRequest);
    }
}
