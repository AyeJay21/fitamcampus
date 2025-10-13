package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow;

import jakarta.persistence.*;

@Entity
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username; // Dein User
    private String followerUrl; // Wer folgt dir

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFollowerUrl() { return followerUrl; }
    public void setFollowerUrl(String followerUrl) { this.followerUrl = followerUrl; }
}
