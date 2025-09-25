package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InboxRepository extends JpaRepository<Inbox, Long> {
    List<Inbox> findByUsername(String username);
}
