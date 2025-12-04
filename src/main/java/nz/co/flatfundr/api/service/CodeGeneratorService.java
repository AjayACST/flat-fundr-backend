package nz.co.flatfundr.api.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CodeGeneratorService {

    public static String generateCode(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

        base64 = base64.replaceAll("[^A-Za-z0-9]", "");
        return base64.substring(0, 6);
    }
}
