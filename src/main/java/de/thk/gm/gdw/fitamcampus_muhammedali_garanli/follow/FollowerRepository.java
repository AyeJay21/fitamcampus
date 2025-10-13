package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByUsername(String username);
}
