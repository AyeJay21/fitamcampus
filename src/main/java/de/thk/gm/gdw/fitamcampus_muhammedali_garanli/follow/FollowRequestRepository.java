package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
    List<FollowRequest> findByUsername(String username);
}
