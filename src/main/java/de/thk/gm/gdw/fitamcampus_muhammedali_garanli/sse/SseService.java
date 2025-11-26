package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
	private static final Logger log = LoggerFactory.getLogger(SseService.class);
	private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

	public void register(String roomId, SseEmitter emitter) {
		emitters.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(emitter);
		log.info("SSE register: room={} totalEmitters={}", roomId, emitters.get(roomId).size());
	}

	public void remove(String roomId, SseEmitter emitter) {
		var list = emitters.get(roomId);
		if (list != null) {
			list.remove(emitter);
			log.info("SSE remove: room={} totalEmitters={}", roomId, list.size());
		}
	}

	public void pushToRoom(String roomId, Object payload) {
		var list = emitters.get(roomId);
		if (list == null) {
			log.debug("SSE pushToRoom: no subscribers for room={}", roomId);
			return;
		}
		log.info("SSE pushToRoom: room={} subscribers={} payloadPreview={}", roomId, list.size(), payload == null ? "null" : payload.toString());
		for (SseEmitter e : list) {
			try {
				e.send(SseEmitter.event().name("message").data(payload));
			} catch (Exception ex) {
				log.warn("SSE send failed for room={}, removing emitter: {}", roomId, ex.getMessage());
				try { e.completeWithError(ex); } catch (Exception ignored) {}
				list.remove(e);
			}
		}
	}
}
