package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.message;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(String sender, String reciever, String text, Date timeStamp){
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
            Message message = new Message();
            message.setSender(sender);
            message.setReciever(reciever);
            message.setText(text);
            message.setTimeStamp(timeStamp);
            messageRepository.save(message);
    }

}
