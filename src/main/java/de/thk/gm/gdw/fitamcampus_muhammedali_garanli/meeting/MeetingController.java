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

import java.util.*;

@Controller
@RequestMapping("/meetings")
public class MeetingController {

    public final MeetingService meetingService;
    public final CommentService commentService;
    public final WeatherService weatherService;

    @Autowired
    public MeetingController(MeetingService meetingService, CommentService commentService, WeatherService weatherService) {
        this.meetingService = meetingService;
        this.commentService = commentService;
        this.weatherService = weatherService;
    }

    @ResponseStatus(HttpStatus.FOUND)
    @PostMapping
    public String addMeeting(Meeting meeting, Model model, HttpSession session) {
        String username = LoginController.getCurrentUser(session).getUsername();
        meeting.setCreatedBy(username);
        model.addAttribute("meeting", meetingService.createMeeting(meeting));
        return "redirect:/meetings/" + meeting.getId();
    }

    @GetMapping
    public String getAll(Model model, HttpSession session) throws Exception {
        if (!LoginController.isLoggedIn(session)) {
            return "redirect:/login";
        }
        String username = LoginController.getCurrentUser(session).getUsername();
        Iterable<Meeting> meetings = meetingService.getAllMeetingsForUser(username);
        Map<String, Weather> weathers = new HashMap<>();
        if (meetings == null) {
            throw new Exception("Meeting ist leer");
        }

        for (Meeting meeting : meetings) {
            try {
                Weather weather = weatherService.getWeather(meeting.date, meeting.getOrt(), meeting.time);
                if (weather == null) {
                    throw new Exception("Wetter liefert keine Daten");
                }
                weathers.put(meeting.getId().toString(), weather);
                System.out.println("WeatherMeetingId: " + meeting.getId() + "Weather: " + weather.getTemperature());
            } catch (Exception e) {
                System.out.println("Wetter konnte für ein Meeting nicht geladen werden: " + e.getMessage());
            }
        }
        model.addAttribute("weathers", weathers);
        model.addAttribute("meetings", meetings);
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
        try {
            Weather weather = weatherService.getWeather(meeting.date, meeting.getOrt(), meeting.time);
            if (weather == null) {
                throw new Exception("Wetter liefert keine Daten");
            }
            model.addAttribute("weathers", weather);
            model.addAttribute("latitude", String.valueOf(weather.getLatitude()).replace(',', '.'));
            model.addAttribute("longitude", String.valueOf(weather.getLongitude()).replace(',', '.'));
        }catch(Exception e){
            System.out.println("Wetter konnte für ein Meeting nicht geladen werden: " + e.getMessage());
        }
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