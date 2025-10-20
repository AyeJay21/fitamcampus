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

        if (path == null || path.isEmpty()) {
            path = "/";
        }

        HttpSignatureService.SignatureResult signatureResult = httpSignatureService.sign("POST", path, host, body, privateKeyPem, actorId);

        System.out.println("=================== ACTIVITYPUB DEBUG ===================");
        System.out.println("Target URL: " + targetInbox);
        System.out.println("Host: '" + host + "'");
        System.out.println("Path: '" + path + "'");
        System.out.println("Date: " + signatureResult.date);
        System.out.println("Digest: " + signatureResult.digest);
        System.out.println("Signature: " + signatureResult.signature);
        System.out.println("Actor ID: " + actorId);
        System.out.println("Body Length: " + body.length() + " characters");
        System.out.println("Body Preview: " + body.substring(0, Math.min(200, body.length())) + "...");
        System.out.println("========================================================");

        // WebClient richtig konfigurieren - nur Schema+Host als baseUrl
        String baseUrl = url.getProtocol() + "://" + host;
        if (url.getPort() != -1) {
            baseUrl += ":" + url.getPort();
        }
        
        WebClient clientFixed = WebClient.builder().baseUrl(baseUrl).build();
        
        try {
            System.out.println("Sending HTTP POST to: " + baseUrl + path);
            String response = clientFixed.post()
                    .uri(path)  // Path separat angeben!
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
            
            System.out.println("SUCCESS! Response from " + host + ": " + response);
        } catch (Exception e) {
            System.err.println("=================== ERROR ===================");
            System.err.println("Failed to send to: " + targetInbox);
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getSimpleName());
            if (e.getCause() != null) {
                System.err.println("Root cause: " + e.getCause().getMessage());
            }
            System.err.println("=============================================");
            throw e;
        }
    }
}
