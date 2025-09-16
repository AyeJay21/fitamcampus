package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.meeting.Meeting;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.meeting.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Override
    public Comment createComment(Comment comment, UUID meetingId) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment must not be null");
        }
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new IllegalArgumentException("Meeting not found"));
        comment.setId(UUID.randomUUID());
        comment.setMeeting(meeting);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Comment getComment(UUID id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public void updateComment(UUID id, Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(UUID id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteAllComments() {
        commentRepository.deleteAll();
    }

    @Override
    public Iterable<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Iterable<Comment> getCommentByMeeting(UUID id) {
        return commentRepository.getCommentByMeetingId(id);
    }

    @Override
    public void deleteCommentByMeeting(UUID id) {
        commentRepository.deleteAll(getCommentByMeeting(id));
    }

    /*@Override
    public Comment getAllCommentsByMeeting(UUID meetingId) {
        return commentRepository.getCommentByMeetingId(meetingId);
    }*/
}
