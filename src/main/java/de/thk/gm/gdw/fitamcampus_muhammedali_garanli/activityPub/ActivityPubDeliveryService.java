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

        URL url = new URL(targetInbox);
        String host = url.getHost();
        String path = url.getPath();

        // Verwende den HttpSignatureService für konsistente Header
        HttpSignatureService.SignatureResult signatureResult = httpSignatureService.sign("POST", path, host, body, privateKeyPem, actorId);

        System.out.println("Sending to: " + targetInbox);
        System.out.println("Host: " + host);
        System.out.println("Path: " + path);
        System.out.println("Date: " + signatureResult.date);
        System.out.println("Digest: " + signatureResult.digest);
        System.out.println("Signature: " + signatureResult.signature);

        WebClient client = WebClient.builder().baseUrl(targetInbox).build();
        
        try {
            String response = client.post()
                    .header("Content-Type", "application/activity+json")
                    .header("Host", host)
                    .header("Date", signatureResult.date)
                    .header("Digest", signatureResult.digest)
                    .header("Signature", signatureResult.signature)
                    .header("User-Agent", "FitamCampus-ActivityPub/1.0")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.err.println("Error sending to inbox: " + e.getMessage());
            throw e;
        }
    }
}
