package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Date;
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

    // Temporary quick-check endpoint: sends one ping event and closes.
    @GetMapping("/sse/ping")
    public SseEmitter ping(@RequestParam(required = false, defaultValue = "test-room") String room) {
        log.info("SSE ping requested for room={}", room);
        SseEmitter emitter = new SseEmitter(5000L);
        try {
            emitter.send(SseEmitter.event().name("ping").data(Map.of("ok", true, "room", room)));
        } catch (Exception e) {
            log.warn("SSE ping send failed: {}", e.getMessage());
        } finally {
            try { emitter.complete(); } catch (Exception ignored) {}
        }
        return emitter;
    }

    // quick test-push: trigger a server-side push into a room
    @GetMapping("/sse/test-push")
    public ResponseEntity<?> testPush(@RequestParam String room, @RequestParam(required = false, defaultValue = "test message") String text) {
        log.info("SSE testPush requested for room={} text={}", room, text);
        Map<String, Object> payload = Map.of(
                "sender", "__server_test__",
                "text", text,
                "timeStamp", new Date().getTime(),
                "room", room
        );
        sseService.pushToRoom(room, payload);
        return ResponseEntity.ok(Map.of("pushed", true));
    }

}
