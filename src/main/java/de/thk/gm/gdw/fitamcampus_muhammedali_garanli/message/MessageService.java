package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Save a message with a lightweight deduplication check: if the most recent message
     * from the same sender to the same receiver has identical text and was created
     * within the last 10 seconds, skip persisting to avoid duplicate entries coming
     * from both outbound and inbound ActivityPub flows.
     */
    public void saveMessage(String sender, String reciever, String text, Date timeStamp) {
        saveMessage(sender, reciever, text, timeStamp, null);
    }

    /**
     * Save with optional ActivityPub activityId for idempotency.
     */
    public void saveMessage(String sender, String reciever, String text, Date timeStamp, String activityId) {
            if (sender == null || sender.isEmpty()) {
                throw new IllegalArgumentException("Sender must not be null or empty");
            }
            if (reciever == null || reciever.isEmpty()) {
                throw new IllegalArgumentException("Reciever must not be null or empty");
            }
            if (text == null || text.isEmpty()) {
                throw new IllegalArgumentException("Text must not be null or empty");
            }
            if (timeStamp == null) {
                throw new IllegalArgumentException("Timestamp must not be null");
            }

            String cleaned = text.replaceAll("<[^>]*>", "");

            // If activityId provided and we've already saved this activity, skip
            if (activityId != null && !activityId.isBlank()) {
                try {
                    Optional<Message> existing = messageRepository.findByActivityId(activityId);
                    if (existing.isPresent()) {
                        log.info("Skipping save: activityId already exists: {}", activityId);
                        return;
                    }
                } catch (Exception e) {
                    log.warn("ActivityId lookup failed, continuing: {}", e.getMessage());
                }
            }

            try {
                Optional<Message> recent = messageRepository.findTopBySenderAndRecieverOrderByTimeStampDesc(sender, reciever);
                if (recent.isPresent()) {
                    Message last = recent.get();
                    if (last.getText() != null && last.getText().equals(cleaned)) {
                        long diff = Math.abs(timeStamp.getTime() - (last.getTimeStamp() != null ? last.getTimeStamp().getTime() : 0L));
                        // if duplicate within 10 seconds, skip
                        if (diff <= 10_000L) {
                            log.info("Skipping duplicate message save for {} -> {} (within {} ms)", sender, reciever, diff);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                // on any error while checking duplicates, fall back to saving the message
                log.warn("Duplicate check failed, proceeding to save message: {}", e.getMessage());
            }

            Message message = new Message();
            message.setSender(sender);
            message.setReciever(reciever);
            message.setText(cleaned);
            message.setTimeStamp(timeStamp);
            if (activityId != null && !activityId.isBlank()) {
                message.setActivityId(activityId);
            }
            messageRepository.save(message);
    }

}
