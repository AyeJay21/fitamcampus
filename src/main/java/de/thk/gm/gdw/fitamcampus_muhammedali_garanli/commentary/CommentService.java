package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment createComment(Comment comment, UUID meetingId);

    Comment getComment(UUID id);

    void updateComment(UUID id, Comment comment);

    void deleteComment(UUID id);

    void deleteAllComments();

    Iterable<Comment> getAllComments();

    Iterable<Comment> getCommentByMeeting(UUID id);

    void deleteCommentByMeeting(UUID id);

    //Comment getAllCommentsByMeeting(UUID meetingId);
}
