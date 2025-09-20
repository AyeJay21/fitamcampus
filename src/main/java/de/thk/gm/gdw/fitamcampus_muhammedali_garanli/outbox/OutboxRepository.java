package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    //List<Outbox> findByUsername(String username);
    //List<Outbox> findAllByActivityJsonContaining(String username);
}
