package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowRequestService {

    public final FollowRequestRepository followRequestRepository;

    public FollowRequestService(FollowRequestRepository followRequestRepository) {
        this.followRequestRepository = followRequestRepository;
    }

    public void deleteFromFollowRequest(String username, String followerUrl) {
        followRequestRepository.deleteByUsernameAndFollowerUrl(username, followerUrl);
    }
}
