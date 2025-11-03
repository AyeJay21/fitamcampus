package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
	private final java.util.concurrent.ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitters = new java.util.concurrent.ConcurrentHashMap<>();

	public void register(String roomId, SseEmitter emitter) {
		emitters.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(emitter);
	}

	public void remove(String roomId, SseEmitter emitter) {
		var list = emitters.get(roomId);
		if (list != null) list.remove(emitter);
	}

	public void pushToRoom(String roomId, Object payload) {
		var list = emitters.get(roomId);
		if (list == null) return;
		for (SseEmitter e : list) {
			try {
				e.send(SseEmitter.event().name("message").data(payload));
			} catch (Exception ex) {
				try { e.completeWithError(ex); } catch (Exception ignored) {}
				list.remove(e);
			}
		}
	}
}
