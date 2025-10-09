package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

@Service
public class ActorService {

    @Autowired
    public ActorRepository actorRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    public Actor createActor(String username,String email, String password) throws Exception{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getMimeEncoder().encodeToString(keyPair.getPublic().getEncoded()) +
                "\n-----END PUBLIC KEY-----";

        String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getMimeEncoder().encodeToString(keyPair.getPrivate().getEncoded()) +
                "\n-----END PRIVATE KEY-----";

        Actor actor = new Actor();
        actor.setUsername(username);
        actor.setPublicKeyPem(publicKeyPem);
        actor.setPrivateKeyPem(privateKeyPem);
        actor.setEmail(email);
        actor.setPasswordHashed(passwordEncoder.encode(password));
        actor.setInboxUrl("https://activitypub.alluneedspot.com/users/" + username + "/inbox");
        actor.setOutboxUrl("https://activitypub.alluneedspot.com/users/" + username + "/outbox");
        actor.setFollowersUrl("https://activitypub.alluneedspot.com/users/" + username + "/followers");
        actor.setFollowingUrl("https://activitypub.alluneedspot.com/users/" + username + "/following");

        return actorRepository.save(actor);
    }

    public Actor getActorByUsername(String username) {
        return actorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Actor not found: " + username));
    }

    public Actor login(String email, String password) throws Exception {
        Actor actor = actorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Benutzer mit dieser Email nicht gefunden"));
        
        if (!passwordEncoder.matches(password, actor.getPasswordHashed())) {
            throw new RuntimeException("Falsches Passwort");
        }
        
        return actor;
    }
    
    public Actor findByEmail(String email) {
        return actorRepository.findByEmail(email).orElse(null);
    }
}
