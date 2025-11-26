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

import static java.security.MessageDigest.getInstance;

@Service
public class HttpSignatureService {

    public static class SignatureResult {
        public final String signature;
        public final String date;
        public final String digest;

        public SignatureResult(String signature, String date, String digest) {
            this.signature = signature;
            this.date = date;
            this.digest = digest;
        }
    }

    public SignatureResult sign(String method, String path, String host, String body, String privateKeyPem, String actorId) throws Exception {

        // Digest des Bodys
        String digest = "SHA-256=" + Base64.getEncoder().encodeToString(getInstance("SHA-256")
                        .digest(body.getBytes(StandardCharsets.UTF_8))
        );

        // Aktuelles Datum RFC-1123 - UTC verwenden mit korrektem Format
        String date = ZonedDateTime.now(java.time.ZoneOffset.UTC)
            .format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", java.util.Locale.ENGLISH));

        // Signing String - exakte Reihenfolge ist wichtig
        String signingString = "(request-target): " + method.toLowerCase() + " " + path + "\n" +
                "host: " + host + "\n" +
                "date: " + date + "\n" +
                "digest: " + digest;

        // Debug-Ausgabe
        System.out.println("Signing String:");
        System.out.println(signingString);

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

        // HTTP Signature Header erstellen - exakt wie Mastodon es erwartet
        String signatureHeader = String.format(
            "keyId=\"%s#main-key\",algorithm=\"rsa-sha256\",headers=\"(request-target) host date digest\",signature=\"%s\"",
            actorId, sigBase64
        );

        return new SignatureResult(signatureHeader, date, digest);
    }
}