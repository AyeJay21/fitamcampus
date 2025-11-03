package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class SseController {

    private static final Logger log = LoggerFactory.getLogger(SseController.class);

    @Autowired
    public SseService sseService;

    @GetMapping("/sse/stream")
    public SseEmitter stream(@RequestParam String room) {
        log.info("SSE stream requested for room={}", room);
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        sseService.register(room, emitter);
        emitter.onCompletion(() -> {
            log.info("SSE emitter completed for room={}", room);
            sseService.remove(room, emitter);
        });
        emitter.onTimeout(() -> {
            log.info("SSE emitter timeout for room={}", room);
            sseService.remove(room, emitter);
        });
        return emitter;
    }

}
