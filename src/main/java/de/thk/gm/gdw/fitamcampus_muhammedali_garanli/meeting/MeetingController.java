package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.meeting;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.navigation.LoginController;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary.Comment;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary.CommentService;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.weather.Weather;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final CommentService commentService;
    public final WeatherService weatherService;

    @Autowired
    public MeetingController(MeetingService meetingService, CommentService commentService,WeatherService weatherService) {
        this.meetingService = meetingService;
        this.commentService = commentService;
        this.weatherService = weatherService;
    }

    @ResponseStatus(HttpStatus.FOUND)
    @PostMapping
    public String addMeeting(Meeting meeting, Model model) {
        model.addAttribute("meeting", meetingService.createMeeting(meeting));
        return "redirect:/meetings/" + meeting.getId();
    }

    @GetMapping
    public String getAll(Model model, HttpSession session) {
        // Login-Check
        if (!LoginController.isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        model.addAttribute("meetings", meetingService.getAllMeetings());
        model.addAttribute("currentUser", LoginController.getCurrentUser(session));
        return "allMeetings";
    }

    @GetMapping("/{id}/edit")
    public String getMeetingEdit(@PathVariable("id") UUID id, Model model) {
        Meeting meeting = meetingService.getMeeting(id);
        if (meeting == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        model.addAttribute("meeting", meeting);
        return "editMeeting";
    }

    @GetMapping("/{id}")
    public String getMeeting(@PathVariable("id") UUID id, Model model) throws Exception {
        Meeting meeting = meetingService.getMeeting(id);
        if (meeting == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        model.addAttribute("meeting", meeting);
        model.addAttribute("comments", commentService.getCommentByMeeting(id));
        Weather weather = weatherService.getWeather(meeting.date, meeting.getOrt(),meeting.time);
        if(weather == null){
            throw new Exception("Wetter liefert keine Daten");
        }
        model.addAttribute("weathers", weather);
        model.addAttribute("latitude", String.valueOf(weather.getLatitude()).replace(',', '.'));
        model.addAttribute("longitude", String.valueOf(weather.getLongitude()).replace(',', '.'));
        return "overview";
    }

    @PatchMapping("/{id}")
    public String updateMeeting(@PathVariable("id") UUID id, Meeting meeting, Model model) {
        Meeting existingMeeting = meetingService.getMeeting(id);
        if (existingMeeting == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        model.addAttribute(meetingService.updateMeeting(id, meeting));
        return "redirect:/meetings/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteMeeting(@PathVariable("id") UUID id) {
        Meeting meeting = meetingService.getMeeting(id);
        if (meeting == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        commentService.deleteCommentByMeeting(id);
        meetingService.deleteMeeting(id);
        return "meetingSite";
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllMeetings() {
        meetingService.deleteAllMeetings();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/comments")
    public String getCommentsByMeetingId(@PathVariable("id") UUID id, Model model) {
        if (meetingService.getMeeting(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found");
        }
        Meeting meeting = meetingService.getMeeting(id);
        Iterable<Comment> comment = commentService.getCommentByMeeting(id);
        model.addAttribute("meeting", meeting);
        model.addAttribute("comments", comment);
        return "overview";
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