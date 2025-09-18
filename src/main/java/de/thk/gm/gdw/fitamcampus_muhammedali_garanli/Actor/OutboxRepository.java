package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.Actor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    List<Outbox> findByUsername(String username);
    List<Outbox> findAllByActivityJsonContaining(String username);
}
