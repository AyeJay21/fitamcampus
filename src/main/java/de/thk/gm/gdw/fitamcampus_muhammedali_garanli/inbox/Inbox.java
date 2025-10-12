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
    private String activityId;
    private String objectType;
    private String content;
    private String inReplyTo;
    private String objectId;

    @JsonIgnore
    public void setActivity(Map<String, Object> activity) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        this.activityJson = mapper.writeValueAsString(activity);

        if (activity.get("type") != null) this.type = activity.get("type").toString();
        if (activity.get("actor") != null) this.actor = activity.get("actor").toString();
        if (activity.get("id") != null) this.activityId = activity.get("id").toString();
        if (activity.get("object") != null) {
            this.objectData = mapper.writeValueAsString(activity.get("object"));
            Object obj = activity.get("object");
            if (obj instanceof Map) {
                Map<?,?> objectMap = (Map<?,?>) obj;
                if (objectMap.get("type") != null) this.objectType = objectMap.get("type").toString();
                if (objectMap.get("content") != null) this.content = objectMap.get("content").toString();
                if (objectMap.get("inReplyTo") != null) this.inReplyTo = objectMap.get("inReplyTo").toString();
                if (objectMap.get("id") != null) this.objectId = objectMap.get("id").toString();
            }
        }
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInReplyTo() {
        return inReplyTo;
    }

    public void setInReplyTo(String inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}