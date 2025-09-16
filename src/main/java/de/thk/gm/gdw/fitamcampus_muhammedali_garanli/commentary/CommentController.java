package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Controller
@RequestMapping(value = "meetings/{meetingId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @ResponseStatus(HttpStatus.FOUND)
    @PostMapping
    public String addComment(@PathVariable("meetingId") UUID meetingId,Comment comment, Model model) {
        model.addAttribute("comments",commentService.createComment(comment,meetingId));
        return "redirect:/meetings/{meetingId}/comments";
    }

    @GetMapping("/{id}")
    public String getComment(@PathVariable("id") UUID id, Model model) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        model.addAttribute("comments", commentService.getComment(id));

        return "redirect:/meetings/{meetingId}";
    }


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
    public void updateComment(@PathVariable("id") UUID id,Comment comment) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        commentService.updateComment(id, comment);
    }
}