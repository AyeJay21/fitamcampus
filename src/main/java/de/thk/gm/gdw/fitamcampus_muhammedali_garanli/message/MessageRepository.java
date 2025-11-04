package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
	Optional<Message> findTopBySenderAndRecieverOrderByTimeStampDesc(String sender, String reciever);
}
