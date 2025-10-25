package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FollowRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String followerUrl;
    private String type;

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFollowerUrl() { return followerUrl; }
    public void setFollowerUrl(String followerUrl) { this.followerUrl = followerUrl; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
