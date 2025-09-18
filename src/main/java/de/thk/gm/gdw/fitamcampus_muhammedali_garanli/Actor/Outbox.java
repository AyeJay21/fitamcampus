package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.Actor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Outbox {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String activityJson;

    @JsonIgnore
    public void setActivity(Map<String, Object> activity) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        this.activityJson = mapper.writeValueAsString(activity);
    }

    @JsonIgnore
    public Map<String, Object> getActivity() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(this.activityJson, Map.class);
    }

    public String getActivityJson() {
        return activityJson;
    }

    public void setActivityJson(String activityJson) {
        this.activityJson = activityJson;
    }
}
