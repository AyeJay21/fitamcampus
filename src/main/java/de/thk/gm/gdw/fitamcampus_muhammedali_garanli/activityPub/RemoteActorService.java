package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RemoteActorService {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * @param handle z.B. "@user@mastodon.social"
     * @return Inbox URL des remote Actors
     */
    public String resolveActorInbox(String handle) throws Exception {
        String[] parts = handle.replace("@", "").split("@");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid handle: " + handle);

        String username = parts[0];
        String domain = parts[1];

        String webfingerUrl = "https://" + domain + "/.well-known/webfinger?resource=acct:" + username + "@" + domain;

        WebClient client = WebClient.create(webfingerUrl);
        String response = client.get().retrieve().bodyToMono(String.class).block();

        JsonNode root = mapper.readTree(response);
        for (JsonNode link : root.get("links")) {
            if (link.has("rel") && "self".equals(link.get("rel").asText())) {
                return link.get("href").asText(); // Actor-URL
            }
        }
        throw new RuntimeException("Inbox not found for " + handle);
    }
}
