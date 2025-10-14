package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.meeting;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary.Comment;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/meetings", produces = MediaType.APPLICATION_JSON_VALUE)
public class MeetingRestController {

    private final MeetingService meetingService;
    private final CommentService commentService;

    @Autowired
    public MeetingRestController(MeetingService meetingService, CommentService commentService) {
        this.meetingService = meetingService;
        this.commentService = commentService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Meeting> addMeeting(@RequestBody Meeting meeting) {
        meetingService.createMeeting(meeting);
        return ResponseEntity.status(HttpStatus.CREATED).body(meeting);
    }

    @GetMapping
    public ResponseEntity<Iterable<Meeting>> getAll() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meeting> getMeeting(@PathVariable("id") UUID id) {
        Meeting meeting = meetingService.getMeeting(id);
        if (meeting == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        return ResponseEntity.ok(meeting);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable("id") UUID id, @RequestBody Meeting meeting) {
        Meeting existingMeeting = meetingService.getMeeting(id);
        if (existingMeeting == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        meetingService.updateMeeting(id, meeting);
        return ResponseEntity.ok(meeting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable("id") UUID id) {
        Meeting meeting = meetingService.getMeeting(id);
        if (meeting == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        meetingService.deleteMeeting(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllMeetings() {
        meetingService.deleteAllMeetings();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Iterable<Comment>> getCommentsByMeetingId(@PathVariable("id") UUID id) {
        if (meetingService.getMeeting(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        return ResponseEntity.ok(commentService.getCommentByMeeting(id));
    }

    @DeleteMapping("/{id}/comments")
    public ResponseEntity<Void> deleteCommentsByMeetingId(@PathVariable("id") UUID id) {
        if (meetingService.getMeeting(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        commentService.deleteCommentByMeeting(id);
        return ResponseEntity.noContent().build();
    }
}