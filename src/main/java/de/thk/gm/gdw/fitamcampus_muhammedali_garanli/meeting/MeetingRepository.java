package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.meeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MeetingRepository extends CrudRepository<Meeting, UUID> {

}
