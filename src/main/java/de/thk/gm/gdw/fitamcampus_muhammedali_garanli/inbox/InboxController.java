package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.inbox;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users/{username}/inbox")
public class InboxController {

    public final InboxRepository inboxRepository;

    public InboxController(InboxRepository inboxRepository){
        this.inboxRepository = inboxRepository;
    }

    @GetMapping(produces = "application/json")
    public List<Inbox> getInbox(@PathVariable String username) {
        return inboxRepository.findAll();
    }

    @PostMapping(consumes = {"application/json", "application/activity+json"})
    public Inbox addToInbox(@PathVariable String username, @RequestBody Map<String, Object> activity) throws Exception {
        Inbox inbox = new Inbox();
        inbox.setActivity(activity);
        inbox.setUsername(username);
        inbox.setType((String) activity.get("type"));
        inbox.setActor((String) activity.get("actor"));
        inbox.setObjectData(activity.get("object").toString());

        return inboxRepository.save(inbox);
    }
}