package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.inbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow.FollowerRepository;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.follow.FollowerService;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.Message;
import de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static io.netty.util.internal.SystemPropertyUtil.contains;

@Controller
@RequestMapping("/users/{username}/inbox")
public class InboxController {

    public final InboxRepository inboxRepository;

    public InboxController(InboxRepository inboxRepository,
                           FollowerRepository followerRepository) {
        this.inboxRepository = inboxRepository;
        this.followerRepository = followerRepository;
    }

    public FollowerService followerService;

    @Autowired
    public MessageService messageService;
    private final FollowerRepository followerRepository;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Inbox> getInbox(@PathVariable String username) {
        return inboxRepository.findByUsername(username);
    }

    @PostMapping(consumes = {"application/json", "application/activity+json","application/ld+json"})
    @ResponseBody
    public Map<String, Object> addToInbox(@PathVariable String username, @RequestBody Map<String, Object> activity) throws Exception {
        System.out.println("Empfangene Activity: " + activity);

        Inbox inbox = new Inbox();
        Message message = new Message();
        ObjectMapper mapper = new ObjectMapper();
        String reciever = "";

        inbox.setActivity(activity);
        inbox.setUsername(username);
        String type = (String) activity.get("type");
        inbox.setType(type);
        inbox.setActor((String) activity.get("actor"));

        inbox.setObjectData(activity.get("object").toString());
        if (activity.get("object") instanceof Map<?, ?> objectMap) {
            inbox.setObjectData(mapper.writeValueAsString(objectMap));
            inbox.setObjectType((String) objectMap.get("type"));
            inbox.setContent((String) objectMap.get("content"));
            inbox.setAttributedTo((String) objectMap.get("attributedTo"));
            inbox.setPublished((String) objectMap.get("published"));
            inbox.setTo((objectMap.get("to") != null) ? objectMap.get("to").toString() : null);

            if ("Create".equals(type) && "Note".equals(objectMap.get("type"))) {
                String sender = (String) activity.get("actor");
                String receiver = null;

                Object toObj = objectMap.get("to");
                if (toObj instanceof List<?> toList && !toList.isEmpty()) {
                    receiver = toList.get(0).toString();
                } else if (toObj instanceof String s) {
                    receiver = s;
                }

                String text = (String) objectMap.get("content");
                Date date = null;
                if (objectMap.get("published") != null) {
                    date = Date.from(Instant.parse(objectMap.get("published").toString()));
                }

                messageService.saveMessage(sender, receiver, text, date);
            }
            System.out.println("========================================");
            if ("Follow".equals(type)) {
                String followerUrl = (String) activity.get("actor");
                System.out.println("================= FOLLOWER URL=======================: " + followerUrl);
                followerService.saveOutsideFollowRequest(username, followerUrl, type);
            }
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