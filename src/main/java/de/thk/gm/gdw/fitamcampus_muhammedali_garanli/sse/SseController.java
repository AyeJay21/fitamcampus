package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class SseController {

    @Autowired
    public SseService sseService;

    @GetMapping("/sse/stream")
    public SseEmitter stream(@RequestParam String room) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        sseService.register(room, emitter);
        emitter.onCompletion(() -> sseService.remove(room,emitter));
        emitter.onTimeout(() -> sseService.remove(room, emitter));
        return emitter;
    }

}
