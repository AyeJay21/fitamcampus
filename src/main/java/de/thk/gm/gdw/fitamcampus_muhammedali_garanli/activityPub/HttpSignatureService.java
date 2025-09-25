package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.activityPub;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
@Service
public class HttpSignatureService {

    public String sign(String method, String path, String host, String body, String privateKeyPem, String actorId) throws Exception {

        // Digest des Bodys
        String digest = "SHA-256=" + Base64.getEncoder().encodeToString(
                java.security.MessageDigest.getInstance("SHA-256")
                        .digest(body.getBytes(StandardCharsets.UTF_8))
        );

        // Aktuelles Datum RFC-1123
        String date = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);

        // Hier wird der signingString korrekt erzeugt
        String signingString = "(request-target): " + method.toLowerCase() + " " + path + "\n" +
                "host: " + host + "\n" +
                "date: " + date + "\n" +
                "digest: " + digest;

        // Private Key laden
        String privateKeyClean = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyClean);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        // Signatur erzeugen
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(signingString.getBytes(StandardCharsets.UTF_8));
        String sigBase64 = Base64.getEncoder().encodeToString(signature.sign());

        // HTTP Signature Header zurückgeben (dynamische keyId!)
        return "keyId=\"" + actorId + "#main-key\","
                + "headers=\"(request-target) host date digest\","
                + "signature=\"" + sigBase64 + "\"";
    }
}
