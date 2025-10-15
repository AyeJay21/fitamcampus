package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.inbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/users/{username}/inbox")
public class InboxController {

    public final InboxRepository inboxRepository;

    public InboxController(InboxRepository inboxRepository){
        this.inboxRepository = inboxRepository;
    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Inbox> getInbox(@PathVariable String username) {
        return inboxRepository.findByUsername(username);
    }

    @PostMapping(consumes = {"application/json", "application/activity+json"})
    @ResponseBody
    public Map<String, Object> addToInbox(@PathVariable String username, @RequestBody Map<String, Object> activity) throws Exception {
        System.out.println("Empfangene Activity: " + activity);

        Inbox inbox = new Inbox();
        ObjectMapper mapper = new ObjectMapper();

        inbox.setActivity(activity);
        inbox.setUsername(username);
        String type = (String) activity.get("type");
        inbox.setType(type);
        inbox.setActor((String) activity.get("actor"));

        inbox.setObjectData(activity.get("object").toString());
        if (activity.get("object") instanceof Map<?,?> objectMap) {
            inbox.setObjectData(mapper.writeValueAsString(objectMap));
            inbox.setObjectType((String) objectMap.get("type"));
            inbox.setContent((String) objectMap.get("content"));
            inbox.setAttributedTo((String) objectMap.get("attributedTo"));
            inbox.setPublished((String) objectMap.get("published"));
            inbox.setTo((objectMap.get("to") != null) ? objectMap.get("to").toString() : null);
        }

        Inbox saved = inboxRepository.save(inbox);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Activity received");
        response.put("activityType", type != null ? type : "");
        response.put("from", inbox.getActor() != null ? inbox.getActor() : "");
        response.put("objectType", inbox.getObjectType() != null ? inbox.getObjectType() : "");
        response.put("content", inbox.getContent() != null ? inbox.getContent() : "");
        return response;
    }
}