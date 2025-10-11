package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

public class SendFollowRequest {
    private String targetHandle;
    private String fromUser;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getTargetHandle() {
        return targetHandle;
    }

    public void setTargetHandle(String targetHandle) {
        this.targetHandle = targetHandle;
    }
}
