package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.meeting;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Meeting {

    @Id
    public UUID id;
    public String name;
    public String ort;
    public LocalDate date;
    public String time;
    public String sportArt;
    public String inOrOut;
    public String description;

    public Meeting() {
    }

    public Meeting(UUID id, String name, String ort, LocalDate date,String time, String sportArt, String inOrOut, String description) {
        this.id = id;
        this.name = name;
        this.ort = ort;
        this.date = date;
        this.time = time;
        this.sportArt = sportArt;
        this.inOrOut = inOrOut;
        this.description = description;
    }
}