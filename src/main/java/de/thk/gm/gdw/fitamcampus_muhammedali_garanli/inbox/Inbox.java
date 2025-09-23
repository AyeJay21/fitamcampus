package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.inbox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;
import java.util.Map;
@Entity
public class Inbox {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String activityJson;

    private String username;
    private String type;
    private String actor;
    private String objectData;

    @JsonIgnore
    public void setActivity(Map<String, Object> activity) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        this.activityJson = mapper.writeValueAsString(activity);

        // Extrahiere nützliche Felder
        if (activity.get("type") != null) this.type = activity.get("type").toString();
        if (activity.get("actor") != null) this.actor = activity.get("actor").toString();
        if (activity.get("object") != null) this.objectData = mapper.writeValueAsString(activity.get("object"));
    }

    @JsonIgnore
    public Map<String, Object> getActivity() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(this.activityJson, Map.class);
    }

    // Getter & Setter
    public Long getId() { return id; }
    public String getActivityJson() { return activityJson; }
    public void setActivityJson(String activityJson) { this.activityJson = activityJson; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }

    public String getObjectData() { return objectData; }
    public void setObjectData(String objectData) { this.objectData = objectData; }
}