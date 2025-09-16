package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meetings/{meetingId}/comments")
public class CommentRestController {

    private final CommentService commentService;
    @Autowired
    public CommentRestController(CommentService commentService){
        this.commentService = commentService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addComment(@RequestBody Comment comment, @PathVariable("meetingId") UUID meetingId) {
        commentService.createComment(comment, meetingId);
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable("id") UUID id) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        return commentService.getComment(id);
    }

/*    @GetMapping
    @ResponseBody
    public List<Comment> getAllComments(){
        return commentService.getAllComments();
    }*/

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable("id") UUID id) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        commentService.deleteComment(id);
    }

    @DeleteMapping
    public void deleteAllComments() {
        commentService.deleteAllComments();
    }

    @PatchMapping("/{id}")
    public void updateComment(@PathVariable("id") UUID id, @RequestBody Comment comment) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        commentService.updateComment(id, comment);
    }
}