package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowRequestService {

    public final FollowRequestRepository followRequestRepository;

    public FollowRequestService(FollowRequestRepository followRequestRepository) {
        this.followRequestRepository = followRequestRepository;
    }

    @Transactional
    public void deleteFromFollowRequest(String username, String followerUrl) {
        followRequestRepository.deleteByUsernameAndFollowerUrl(username, followerUrl);
    }
}
