package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.commentary;

import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.meeting.Meeting;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    public UUID id;
    public String name;
    public String kommentar;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    public Meeting meeting;

    public Comment() {
    }

    public Comment(UUID id, String kommentar, Meeting meeting, String name) {
        this.id = id;
        this.kommentar = kommentar;
        this.name = name;
        this.meeting = meeting;
    }
}