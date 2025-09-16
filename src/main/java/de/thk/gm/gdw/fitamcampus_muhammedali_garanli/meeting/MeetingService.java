package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.meeting;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary.Comment;

import java.util.List;
import java.util.UUID;

public interface MeetingService {
    Meeting createMeeting(Meeting meeting);

    Meeting getMeeting(UUID id);

    Meeting updateMeeting(UUID id, Meeting meeting);

    void deleteMeeting(UUID id);

    void deleteAllMeetings();

    Iterable<Meeting> getAllMeetings();

}
