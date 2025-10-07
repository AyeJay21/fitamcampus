package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RemoteActorService {

    private final ObjectMapper mapper = new ObjectMapper();

    public String resolveActorInbox(String handle) throws Exception {
        String normalizedHandle = handle.startsWith("@") ? handle.substring(1) : handle;
        String[] parts = normalizedHandle.split("@");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid handle: " + handle);

        String username = parts[0];
        String domain = parts[1];

        String webfingerUrl = "https://" + domain + "/.well-known/webfinger?resource=acct:" + username + "@" + domain;
        WebClient client = WebClient.create(webfingerUrl);
        String response = client.get().retrieve().bodyToMono(String.class).block();

        JsonNode root = mapper.readTree(response);
        String actorUrl = null;
        for (JsonNode link : root.get("links")) {
            if ("self".equals(link.get("rel").asText()) && "application/activity+json".equals(link.get("type").asText())) {
                actorUrl = link.get("href").asText();
                break;
            }
        }
        if (actorUrl == null) throw new RuntimeException("Actor URL not found for " + handle);

        String actorJson = WebClient.create(actorUrl)
                .get()
                .header("Accept", "application/activity+json")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode actorNode = mapper.readTree(actorJson);

        if (actorNode.has("endpoints") && actorNode.get("endpoints").has("sharedInbox")) {
            return actorNode.get("endpoints").get("sharedInbox").asText();
        }
        if (actorNode.has("inbox")) {
            return actorNode.get("inbox").asText();
        }

        throw new RuntimeException("No inbox found for actor: " + actorUrl);
    }

    public String resolveActorUrl(String handle) throws Exception {
        String normalizedHandle = handle.startsWith("@") ? handle.substring(1) : handle;
        String[] parts = normalizedHandle.split("@");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid handle: " + handle);

        String username = parts[0];
        String domain = parts[1];

        // WebFinger abrufen
        String webfingerUrl = "https://" + domain + "/.well-known/webfinger?resource=acct:" + username + "@" + domain;
        WebClient client = WebClient.create(webfingerUrl);
        String response = client.get().retrieve().bodyToMono(String.class).block();

        JsonNode root = mapper.readTree(response);
        for (JsonNode link : root.get("links")) {
            if ("self".equals(link.get("rel").asText()) && "application/activity+json".equals(link.get("type").asText())) {
                return link.get("href").asText();
            }
        }
        throw new RuntimeException("Actor URL not found for " + handle);
    }
}
