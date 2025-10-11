package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import java.util.List;
import java.util.UUID;

public class SendNoteRequest {
    private String message;
    private String targetHandle;
    private String fromUser;
    private String instanceType;
    private List<UUID> meetingIds;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTargetHandle() {
        return targetHandle;
    }

    public void setTargetHandle(String targetHandle) {
        this.targetHandle = targetHandle;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public List<UUID> getMeetingIds() {
        return meetingIds;
    }

    public void setMeetingIds(List<UUID> meetingIds) {
        this.meetingIds = meetingIds;
    }
}