package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.actor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findByUsername(String username);
    Optional<Actor> findByEmail(String email);
    Optional<Actor> findUsernameByEmail(String email);
}