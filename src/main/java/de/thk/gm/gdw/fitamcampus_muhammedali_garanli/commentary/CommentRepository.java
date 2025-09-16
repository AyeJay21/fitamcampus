package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Iterable<Comment> getCommentByMeetingId(UUID meetingId);
    List<Comment> deleteCommentByMeetingId(UUID meetingId);
}
