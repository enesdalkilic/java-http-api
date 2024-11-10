package com.ensd.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//  ────── HS256 ────── //
public class JWTUtil {

    private static final String SECRET_KEY = "your-256-bit-secret";  // Replace with your secret key

    public static String createJWT(String payload) throws Exception {
        // Step 1: Create the Header
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        // Step 2: Encode Header and Payload
        String encodedHeader = base64UrlEncode(header);
        String encodedPayload = base64UrlEncode(payload);

        // Create the Signature
        String signature = createHmacSignature(encodedHeader + "." + encodedPayload, SECRET_KEY);

        // Concatenate all parts to form the JWT
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    // Decodes and verifies a JWT
    public static boolean verifyJWT(String jwt) throws Exception {
        // Step 1: Split the JWT into its components
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        String encodedHeader = parts[0];
        String encodedPayload = parts[1];
        String signature = parts[2];

        // Step 2: Decode Header and Payload
        String header = new String(Base64.getUrlDecoder().decode(encodedHeader), StandardCharsets.UTF_8);
        String payload = new String(Base64.getUrlDecoder().decode(encodedPayload), StandardCharsets.UTF_8);

//        System.out.println("Decoded Header: " + header);
//        System.out.println("Decoded Payload: " + payload);
        try {
            JSONObject payloadObject = new JSONObject(payload);
            Object expireDateInMs = payloadObject.get("expireDate");
            try {
                Long.parseLong((String) expireDateInMs);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
            long currentEpoch = new Date().getTime();
            Date until = new Date(currentEpoch + Long.parseLong("100000000")); //(String) expireDateInMs)
            System.out.println("until " + until);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Step 3: Verify the Signature
        String expectedSignature = createHmacSignature(encodedHeader + "." + encodedPayload, SECRET_KEY);

        return expectedSignature.equals(signature);
    }

    // Helper method for Base64 URL encoding without padding
    private static String base64UrlEncode(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    // Method to create HMAC SHA-256 signature
    private static String createHmacSignature(String data, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKeySpec);
        byte[] signatureBytes = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
    }


}
