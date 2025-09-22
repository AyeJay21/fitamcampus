package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;

import java.util.Map;

@Service
public class ActivityPubDeliveryService {

    @Autowired
    public HttpSignatureService httpSignatureService;

    public void sendToInbox(String targetInbox, Map<String, Object> activity, String actorId, String privateKeyPem) throws Exception{
        String body = new ObjectMapper().writeValueAsString(activity);

        URL url = new java.net.URL(targetInbox);
        String host = url.getHost();
        String path = url.getPath();

        String signatureHeader = httpSignatureService.sign("POST", path, host, body, privateKeyPem);

        WebClient client = WebClient.builder().baseUrl(targetInbox).build();
        client.post()
                .header("Content-Type", "application/activity+json")
                .header("Signature", signatureHeader)
                .header("Date", java.time.ZonedDateTime.now().format(java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
