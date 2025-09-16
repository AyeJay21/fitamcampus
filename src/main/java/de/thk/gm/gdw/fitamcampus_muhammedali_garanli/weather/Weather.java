package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.weather;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Weather {
    double temperature;
    String weatherType;
    double latitude;
    double longitude;
}
