package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.meeting;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Override
    public Meeting createMeeting(Meeting meeting) {
        if (meeting == null) {
            throw new IllegalArgumentException("Meeting must not be null");
        }
        UUID print = UUID.randomUUID();
        meeting.setId(print);
        meeting.setName(meeting.name);
        meeting.setOrt(meeting.ort);
        meeting.setDate(meeting.date);
        meeting.setSportArt(meeting.sportArt);
        meeting.setInOrOut(meeting.inOrOut);
        meeting.setDescription(meeting.description);
        meeting.setTime(meeting.time);
        System.out.println(print);
        meetingRepository.save(meeting);
        return meeting;
    }

    @Override
    public Meeting getMeeting(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Meeting ID must not be null");
        }
        return meetingRepository.findById(id).orElse(null);
    }

    @Override
    public Meeting updateMeeting(UUID id, Meeting meeting) {
        if (id == null) {
            throw new IllegalArgumentException("Meeting ID must not be null");
        }
        Meeting existingMeeting = meetingRepository.findById(id).orElse(null);
        if (existingMeeting == null) {
            throw new IllegalArgumentException("Meeting not found");
        }
        if (meeting.getName() != null) {
            existingMeeting.setName(meeting.getName());
        }
        if (meeting.getOrt() != null) {
            existingMeeting.setOrt(meeting.getOrt());
        }
        if (meeting.getDate() != null) {
            existingMeeting.setDate(meeting.getDate());
        }
        if (meeting.getTime() != null) {
            existingMeeting.setTime(meeting.getTime());
        }
        if (meeting.getSportArt() != null) {
            existingMeeting.setSportArt(meeting.getSportArt());
        }
        if (meeting.getInOrOut() != null) {
            existingMeeting.setInOrOut(meeting.getInOrOut());
        }
        if (meeting.getDescription() != null) {
            existingMeeting.setDescription(meeting.getDescription());
        }
        meetingRepository.save(existingMeeting);
        return existingMeeting;
    }

    @Override
    public void deleteMeeting(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Meeting ID must not be null");
        }
        meetingRepository.deleteById(id);
    }

    @Override
    public void deleteAllMeetings() {
        meetingRepository.deleteAll();
    }

    @Override
    public Iterable<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }
}
