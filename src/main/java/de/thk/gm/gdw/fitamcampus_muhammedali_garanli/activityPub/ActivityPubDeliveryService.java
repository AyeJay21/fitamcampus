package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

@Service
public class ActivityPubDeliveryService {

    @Autowired
    public HttpSignatureService httpSignatureService;

    public void sendToInbox(String targetInbox, Map<String, Object> activity, String actorId, String privateKeyPem) throws Exception{
        String body = new ObjectMapper().writeValueAsString(activity);

        URL url = new URL(targetInbox);
        String host = url.getHost();
        String path = url.getPath();

        String digest = "SHA-256=" + Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("SHA-256").digest(body.getBytes(StandardCharsets.UTF_8))
        );

        String date = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);

        String signatureHeader = httpSignatureService.sign("POST", path, host, body, privateKeyPem, actorId);

        WebClient client = WebClient.builder().baseUrl(targetInbox).build();
        client.post()
                .header("Content-Type", "application/activity+json")
                .header("Host", host)
                .header("Date", date)
                .header("Digest", digest)
                .header("Signature", signatureHeader)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
