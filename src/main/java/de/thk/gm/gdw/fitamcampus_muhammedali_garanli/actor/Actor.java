package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor;

import jakarta.persistence.*;

@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column( nullable = false)
    private String passwordHashed;
    private String displayName;

    @Lob
    private String privateKeyPem;

    @Lob
    private String publicKeyPem;

    private String inboxUrl;
    private String outboxUrl;
    private String followersUrl;
    private String followingUrl;

    public String getInboxUrl() {
        return inboxUrl;
    }

    public void setInboxUrl(String inboxUrl) {
        this.inboxUrl = inboxUrl;
    }

    public String getOutboxUrl() {
        return outboxUrl;
    }

    public void setOutboxUrl(String outboxUrl) {
        this.outboxUrl = outboxUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }

    public String getFollowingUrl() {
        return followingUrl;
    }

    public void setFollowingUrl(String followingUrl) {
        this.followingUrl = followingUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPublicKeyPem() {
        return publicKeyPem;
    }

    public void setPublicKeyPem(String publicKeyPem) {
        this.publicKeyPem = publicKeyPem;
    }

    public String getPrivateKeyPem() {
        return privateKeyPem;
    }

    public void setPrivateKeyPem(String privateKeyPem) {
        this.privateKeyPem = privateKeyPem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHashed() {
        return passwordHashed;
    }

    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed = passwordHashed;
    }
}
